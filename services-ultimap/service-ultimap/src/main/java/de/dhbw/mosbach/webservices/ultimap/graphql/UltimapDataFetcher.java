package de.dhbw.mosbach.webservices.ultimap.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import de.dhbw.mosbach.webservices.ultimap.client.routing.types.RouteType;
import de.dhbw.mosbach.webservices.ultimap.client.weather.types.WeatherType;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.external.routing.IRoutingProvider;
import de.dhbw.mosbach.webservices.ultimap.external.weather.IWeatherProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarCostInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherInfoType;
import de.dhbw.mosbach.webservices.ultimap.util.ConversionUtilKt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class UltimapDataFetcher {
    private final ICarinfoProvider carinfoProvider;
    private final IWeatherProvider weatherProvider;
    private final IRoutingProvider routingProvider;

    @Value("${ultimap.weather.samples:10}")
    private int weatherSamples;

    public UltimapDataFetcher(ICarinfoProvider carinfoProvider, IWeatherProvider weatherProvider, IRoutingProvider routingProvider) {
        this.carinfoProvider = carinfoProvider;
        this.weatherProvider = weatherProvider;
        this.routingProvider = routingProvider;
    }

    @DgsData(parentType = "Query", field = "routeInfo")
    public UltimapType getRouteInfo(UltimapInput input) {
        log.info("RouteInfo was invoked with parameters: {}", input);
        UltimapType toReturn = new UltimapType();

        // Load the RouteInformation
        CoordinateType start = parseInputString(input.getGeopoints().getStart());
        CoordinateType destination = parseInputString(input.getGeopoints().getDestination());
        RouteType receivedRoute = routingProvider.getRoute(start, destination);
        toReturn.setRoute(
                RouteInfoType
                        .newBuilder()
                        .start(input.getGeopoints().getStart())
                        .destination(input.getGeopoints().getDestination())
                        .distance(receivedRoute.getDistance())
                        .duration(receivedRoute.getTime())
                        .waypoints(receivedRoute.getWaypoints().stream().map(ConversionUtilKt::toUltimapCoordinate).collect(Collectors.toList()))
                        .build()
        );

        // Calculate/Load the estimated costs
        toReturn.setCosts(calculateCosts(input, receivedRoute));

        try {
            // Calculate/Load the predicted weather
            toReturn.setWeather(calculateWeather(toReturn.getRoute().getWaypoints(), input.getDeparture(), receivedRoute.getTime()));
        } catch (NullPointerException ex) {
            log.error("Error when looking up weather", ex);
            throw new DgsEntityNotFoundException("Something went wrong when looking up the weather");
        }

        log.debug("Calculated routeInfo: {}", toReturn);
        return toReturn;
    }

    private CarCostInfoType calculateCosts(UltimapInput input, RouteType receivedRoute) {
        log.debug("Calculating costs for Route '{}'", receivedRoute);
        if (input.getFuel() == null) {
            log.warn("Received no fuel-parameter");
            return null;
        }

        double wearFlatrate = (receivedRoute.getDistance() / 1000.0) * 0.06; // 6 ct per kilometre (https://unser-auto.org/fahrkostenrechner/)
        double totalConsumption = receivedRoute.getDistance() * input.getFuel().getConsumption() / (100.0 * 1000.0);
        double fuelCosts = totalConsumption * carinfoProvider.getFuel(input.getFuel().getTyp()).getPrice();

        CarCostInfoType toReturn = CarCostInfoType
                .newBuilder()
                .wearFlatrate(roundToDigits(wearFlatrate, 3))
                .totalConsumption(roundToDigits(totalConsumption, 1))
                .fuelCosts(roundToDigits(fuelCosts, 3))
                .build();
        log.info("Calculated costs: {}", toReturn);
        return toReturn;
    }

    private WeatherInfoType calculateWeather(List<CoordinateType> waypoints, Integer departure, Integer time) {
        // Take only some (default:10) samples of the route to ease the weather service
        if (departure == null || departure == 0) {
            departure = ((int) Instant.now().getEpochSecond());
        }
        final int samples = weatherSamples;
        final double segmentSize = waypoints.size() / ((double) samples);

        WeatherInfoType toReturn = new WeatherInfoType(Double.MAX_VALUE, Double.MIN_VALUE, 0.0, 0.0);

        for (int i = 0 ; i < samples ; i++) {
            int index = (int) (i * segmentSize);
            int pointInTime = (int) (departure + (time * 60 * ((double) i / samples)));

            WeatherType weatherAtCheckpoint = weatherProvider.getWeather(waypoints.get(index), pointInTime);
            log.info("Weather at checkpoint #{}: {}", i, weatherAtCheckpoint);

            double temperature = weatherAtCheckpoint.getTemp();

            if (temperature < toReturn.getMin()) {
                toReturn.setMin(temperature);
            }
            if (temperature > toReturn.getMax()) {
                toReturn.setMax(temperature);
            }
            toReturn.setAvg(toReturn.getAvg() + (temperature / samples));

            // If it rains
            if (weatherAtCheckpoint.getRain() > 0) {
                // then increase the rain coverage for the full route
                toReturn.setRain(toReturn.getRain() + (1.0 / samples));
            }
        }

        toReturn.setMin(roundToDigits(toReturn.getMin(), 3));
        toReturn.setMax(roundToDigits(toReturn.getMax(), 3));
        toReturn.setAvg(roundToDigits(toReturn.getAvg(), 3));
        toReturn.setRain(roundToDigits(toReturn.getRain(), 3));

        log.info("calculated Weather: {}", toReturn);

        return toReturn;
    }

    private CoordinateType parseInputString(String input) {
        log.info("Parsing {} into coordinates", input);
        if (input.matches("\\(\\d+(\\.\\d+)?,\\d+(\\.\\d+)?\\)")) {
            try {
                log.info("Recognized coordinates");
                String[] split = input
                        .subSequence(1, input.length() - 1).toString()
                        .split(",");
                CoordinateType toReturn = CoordinateType.newBuilder()
                                                        .lat(Double.parseDouble(split[0]))
                                                        .lon(Double.parseDouble(split[1]))
                                                        .build();
                log.info("Result: {}", toReturn);
                return toReturn;
            } catch (Exception ex) {
                log.warn("Could not parse \"" + input + "\".", ex);
            }
        }

        try {
            return routingProvider.geocode(input);
        } catch (NullPointerException ex) {
            throw new DgsEntityNotFoundException("Could not Geocode " + input);
        }
    }

    private double roundToDigits(double value, int digits) {
        return Math.round(value * Math.pow(10, digits)) / Math.pow(10, digits);
    }

}
