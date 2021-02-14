package de.dhbw.mosbach.webservices.ultimap;

import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.external.routing.IRoutingProvider;
import de.dhbw.mosbach.webservices.ultimap.external.weather.IWeatherProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.FuelType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.RouteType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.WeatherType;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    ICarinfoProvider getMockCarinfoProvider () {
        return new MockCarinfoProvider();
    }

    @Bean
    IWeatherProvider getMockWeatherProvider () {
        return new MockWeatherProvider();
    }

    @Bean
    IRoutingProvider getMockRoutingProvider () {
        return new MockRoutingProvider();
    }

    static class MockCarinfoProvider implements ICarinfoProvider {
        private final List<CarInfoType> cars = Arrays.asList(
                new CarInfoType(
                        0,
                        "Lamborghini Aventador S",
                        19.0,
                        FuelType.BENZOL
                ),
                new CarInfoType(
                        1,
                        "BMW 3er M",
                        8.7,
                        FuelType.DIESEL
                )
        );

        @Override
        public CarInfoType getCar (int carId) {
            return cars.get(carId);
        }

        @Override
        public FuelPriceType getFuel (FuelType typ) {
            switch (typ) {
                case BENZOL:
                    return new FuelPriceType(1.439);
                case DIESEL:
                    return new FuelPriceType(1.129);
            }

            return null;
        }

        @Override
        public List<CarInfoType> getAllCars () {
            return cars;
        }
    }

    static class MockWeatherProvider implements IWeatherProvider {

        @Override
        public WeatherType getWeather (CoordinateType coordinateType) {
            return new WeatherType(12.4, 4.8);
        }
    }

    static class MockRoutingProvider implements IRoutingProvider {
        @Override
        public CoordinateType geocode (String name) {
            if (name.equals("DHBW Mosbach")) {
                return new CoordinateType(49.351978, 9.145870);
            } else if (name.equals("DHBW Bad Mergentheim")) {
                return new CoordinateType(49.490200, 9.773150);
            }
            return new CoordinateType(49.3533157, 9.1493341);
        }

        @Override
        public RouteType getRoute (CoordinateType start, CoordinateType finish) {
            return new RouteType(
                    60,
                    50000,
                    Arrays.asList(start, finish)
            );
        }
    }
}
