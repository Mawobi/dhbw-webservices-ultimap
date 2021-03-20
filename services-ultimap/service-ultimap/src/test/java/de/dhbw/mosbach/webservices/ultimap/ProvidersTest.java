package de.dhbw.mosbach.webservices.ultimap;

import de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.client.routing.types.RouteType;
import de.dhbw.mosbach.webservices.ultimap.client.weather.types.WeatherType;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.DefaultCarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.external.routing.DefaultRoutingProvider;
import de.dhbw.mosbach.webservices.ultimap.external.routing.IRoutingProvider;
import de.dhbw.mosbach.webservices.ultimap.external.weather.DefaultWeatherProvider;
import de.dhbw.mosbach.webservices.ultimap.external.weather.IWeatherProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import de.dhbw.mosbach.webservices.ultimap.util.ConversionUtilKt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = {
        ServiceUltimapApplication.class,
        DefaultRoutingProvider.class,
        DefaultWeatherProvider.class,
        DefaultCarinfoProvider.class
}
)
public class ProvidersTest {
    @Autowired
    private RestTemplate template;
    private MockRestServiceServer mockServer;

    @Autowired
    private ICarinfoProvider carinfoProvider;
    @Autowired
    private IWeatherProvider weatherProvider;
    @Autowired
    private IRoutingProvider routingProvider;

    @BeforeEach
    void setUp () {
        mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    void carinfoProviderGetCar () {
        mockServer
                .expect(requestTo("http://ultimap-carinfo:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"car\": {\"name\": \"Audi A3\",\"id\": 0,\"typ\": \"BENZOL\",\"consumption\": 6.9}}}", MediaType.APPLICATION_JSON));
        CarInfoType received = carinfoProvider.getCar(0);
        CarInfoType expected = new CarInfoType(0, "Audi A3", 6.9, FuelType.BENZOL);
        assertEquals(expected, received);
    }

    @Test
    void carinfoProviderGetFuel () {
        mockServer
                .expect(requestTo("http://ultimap-carinfo:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"fuel\": {\"price\": 1.449}}}", MediaType.APPLICATION_JSON));
        FuelPriceType received = carinfoProvider.getFuel(FuelType.BENZOL);
        FuelPriceType expected = new FuelPriceType(1.449);
        assertEquals(expected, received);
    }

    @Test
    void carinfoProviderGetAllCars () {
        mockServer
                .expect(requestTo("http://ultimap-carinfo:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"allCars\": [{\"name\": \"Audi A3\",\"id\": 0,\"typ\": \"BENZOL\",\"consumption\": 6.9},{\"name\": \"VW Golf 6\",\"id\": 1,\"typ\": \"DIESEL\",\"consumption\": 7.2}]}}", MediaType.APPLICATION_JSON));
        List<CarInfoType> received = carinfoProvider.getAllCars();
        List<CarInfoType> expected = Arrays.asList(
                new CarInfoType(0, "Audi A3", 6.9, FuelType.BENZOL),
                new CarInfoType(1, "VW Golf 6", 7.2, FuelType.DIESEL)
        );
        assertEquals(expected, received);
    }

    @Test
    void weatherProviderGetWeather () {
        mockServer
                .expect(requestTo("http://ultimap-weather:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"weather\": {\"temp\": 0.8,\"rain\": 1.2}}}", MediaType.APPLICATION_JSON));
        WeatherType received = weatherProvider.getWeather(new CoordinateType(49.351978, 9.145870), (int) Instant.now().getEpochSecond());
        WeatherType expected = new WeatherType(0.8, 1.2);
        assertEquals(expected, received);
    }

    @Test
    void routingProviderGeocode () {
        mockServer
                .expect(requestTo("http://ultimap-routing:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"geocode\": {\"lat\": 49.351978,\"lon\": 9.145870}}}", MediaType.APPLICATION_JSON));
        CoordinateType received = routingProvider.geocode("DHBW Mosbach");
        CoordinateType expected = new CoordinateType(49.351978, 9.145870);
        assertEquals(expected, received);
    }

    @Test
    void routingProviderGetRoute () {
        mockServer
                .expect(requestTo("http://ultimap-routing:8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess("{\"data\": {\"route\": {\"time\": 300,\"distance\": 500000, \"waypoints\": [{\"lat\": 50.0,\"lon\": 9.0},{\"lat\": 51.0,\"lon\": 10.0}]}}}", MediaType.APPLICATION_JSON));
        RouteType received = routingProvider.getRoute(new CoordinateType(50.0, 9.0), new CoordinateType(51.0, 10.0));
        RouteType expected = new RouteType(300, 500_000,
                                           Stream.of(
                                                   new CoordinateType(50.0, 9.0),
                                                   new CoordinateType(51.0, 10.0)
                                           ).map(ConversionUtilKt::toRoutingCoordinate).collect(Collectors.toList()));
        assertEquals(expected, received);
    }
}
