package de.dhbw.mosbach.webservices.routing.external;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import de.dhbw.mosbach.webservices.routing.external.geocoderesponse.OpenRouteServiceSimplifiedGeocodeResponse;
import de.dhbw.mosbach.webservices.routing.external.routeresponse.OpenRouteServiceSimplifiedRouteResponse;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DefaultRoutingProvider implements IRoutingProvider {
    private final RestTemplate restTemplate;
    List<Long> lastRouteRequestsList;
    List<Long> lastGeocodeRequestsList;
    @Value("${ultimap.routing.token}")
    private String apiToken;

    public DefaultRoutingProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.lastRouteRequestsList = new ArrayList<>();
        this.lastGeocodeRequestsList = new ArrayList<>();
    }

    @Override
    public RouteType getRoute(CoordinateInput start, CoordinateInput destination) {

        RouteType routeType = new RouteType();
        routeType.setDistance(64000);
        routeType.setTime(3600);
        CoordinateType dummyCoordinatesDHBWMosbach = new CoordinateType();
        dummyCoordinatesDHBWMosbach.setLat(49.351978);
        dummyCoordinatesDHBWMosbach.setLon(9.145870);
        CoordinateType dummyCoordinatesDHBWBadMergentheim = new CoordinateType();
        dummyCoordinatesDHBWBadMergentheim.setLat(49.490200);
        dummyCoordinatesDHBWBadMergentheim.setLon(9.773150);
        routeType.setWaypoints(new ArrayList<>(Arrays.asList(dummyCoordinatesDHBWMosbach, dummyCoordinatesDHBWBadMergentheim)));

        if (!apiToken.equals("DUMMY")) {

            OpenRouteServiceSimplifiedRouteResponse response = null;

            lastRouteRequestsList.removeIf(aLong -> aLong + 60 < Instant.now().getEpochSecond());

            if (lastRouteRequestsList.size() < 35) {

                lastRouteRequestsList.add(Instant.now().getEpochSecond());
                String url = String.format(
                        "https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s",
                        apiToken, start.getLon(), start.getLat(), destination.getLon(), destination.getLat());
                try {
                    response = restTemplate.getForObject(url, OpenRouteServiceSimplifiedRouteResponse.class);
                } catch (RuntimeException exception) {
                    exception.printStackTrace();
                    throw new DgsEntityNotFoundException("Request to OpenRouteService (Route) failed!");
                }

                log.info("New Request to OpenRouteService (Route)");

            } else {
                log.error("Canceled Request to OpenRouteService (getRoute) to prevent of API-Key overuse.");
            }

            if (response != null) {
                List<CoordinateType> coordinateTypeList = new ArrayList<>();
                for (double[] coordinate : response.getFeatures()[0].getGeometry().getCoordinates()) {
                    CoordinateType coordinateType = new CoordinateType();
                    coordinateType.setLon(coordinate[0]);
                    coordinateType.setLat(coordinate[1]);
                    coordinateTypeList.add(coordinateType);
                }

                routeType.setDistance(response.getFeatures()[0].getProperties().getSummary().getDistance());
                routeType.setTime(response.getFeatures()[0].getProperties().getSummary().getDuration());
                routeType.setWaypoints(coordinateTypeList);
            } else {
                log.error("No Response from OpenRouteService » getRoute");
            }
        }

        return routeType;
    }

    @Override
    public CoordinateType getGeocode(String name) {

        CoordinateType coordinateType = new CoordinateType();
        coordinateType.setLat(49.351978);
        coordinateType.setLon(9.145870);

        if (!apiToken.equals("DUMMY")) {

            OpenRouteServiceSimplifiedGeocodeResponse response = null;

            lastRouteRequestsList.removeIf(aLong -> aLong + 60 < Instant.now().getEpochSecond());

            if (lastRouteRequestsList.size() < 95) {

                lastRouteRequestsList.add(Instant.now().getEpochSecond());
                String url = String.format(
                        "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s",
                        apiToken, URLEncoder.encode(name, StandardCharsets.UTF_8));
                try {
                    response = restTemplate.getForObject(url, OpenRouteServiceSimplifiedGeocodeResponse.class);
                } catch (RuntimeException exception) {
                    exception.printStackTrace();
                    throw new DgsEntityNotFoundException("Request to OpenRouteService (Geocode) failed!");
                }

                log.info("New Request to OpenRouteService (Geocode)");

            } else {
                log.error("Canceled Request to OpenRouteService (getGeocode) to prevent of API-Key overuse.");
            }

            if (response != null) {

                if (response.getFeatures().length > 0) {
                    coordinateType.setLon(response.getFeatures()[0].getGeometry().getCoordinates()[0]);
                    coordinateType.setLat(response.getFeatures()[0].getGeometry().getCoordinates()[1]);
                } else {
                    log.error("Got no Geocode from OpenRouteService for input:" + name);
                }

            } else {
                log.error("No Response from OpenRouteService » getGeocode");
            }
        }

        return coordinateType;
    }
}
