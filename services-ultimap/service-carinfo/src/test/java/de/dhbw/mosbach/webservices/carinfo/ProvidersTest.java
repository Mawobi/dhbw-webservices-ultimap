package de.dhbw.mosbach.webservices.carinfo;

import de.dhbw.mosbach.webservices.carinfo.data.Car;
import de.dhbw.mosbach.webservices.carinfo.data.ICarRepository;
import de.dhbw.mosbach.webservices.carinfo.external.DefaultFuelPriceProvider;
import de.dhbw.mosbach.webservices.carinfo.external.IFuelPriceProvider;
import de.dhbw.mosbach.webservices.carinfo.graphql.CarInfoDataFetcher;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {
        ServiceCarinfoApplication.class,
        DefaultFuelPriceProvider.class,
        CarInfoDataFetcher.class
})
public class ProvidersTest {
    @Autowired
    private RestTemplate template;
    private MockRestServiceServer mockServer;

    @Value("${ultimap.carinfo.fuel.station}")
    private String stationId;
    @Value("${ultimap.carinfo.token}")
    private String apiToken;

    @Autowired
    private IFuelPriceProvider fuelPriceProvider;
    @Autowired
    private ICarRepository carRepository;
    @Autowired
    private CarInfoDataFetcher dataFetcher;

    @BeforeEach
    void setUp () {
        mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    @Order(1)
    void testFuelPriceProvisionCaching () throws InterruptedException {
        // https://creativecommons.tankerkoenig.de/
        String url = String.format("https://creativecommons.tankerkoenig.de/json/prices.php?ids=%s&apikey=%s", stationId, apiToken);
        mockServer
                .expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTankerkoenigResponse(), MediaType.APPLICATION_JSON));
        assertEquals(1.439, fuelPriceProvider.getFuelPrice(FuelType.BENZOL));
        // Get a second value, that should be cached
        assertEquals(1.219, fuelPriceProvider.getFuelPrice(FuelType.DIESEL));

        // Should still be Cached
        Thread.sleep(2000);
        assertEquals(1.439, fuelPriceProvider.getFuelPrice(FuelType.BENZOL));
        ((DefaultFuelPriceProvider) fuelPriceProvider).resetCache();
    }

    @Test
    @Order(2)
    void testFuelPriceProvisionCacheInvalidating () throws InterruptedException {
        // https://creativecommons.tankerkoenig.de/
        String url = String.format("https://creativecommons.tankerkoenig.de/json/prices.php?ids=%s&apikey=%s", stationId, apiToken);
        mockServer
                .expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTankerkoenigResponse(), MediaType.APPLICATION_JSON));
        mockServer
                .expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTankerkoenigResponse(), MediaType.APPLICATION_JSON));

        assertEquals(1.439, fuelPriceProvider.getFuelPrice(FuelType.BENZOL));
        // Get a second value, that should be cached
        assertEquals(1.219, fuelPriceProvider.getFuelPrice(FuelType.DIESEL));

        // Cache is now invalid
        Thread.sleep(6000);
        assertEquals(1.219, fuelPriceProvider.getFuelPrice(FuelType.DIESEL));
        mockServer.verify();
    }

    @Test
    @Order(3)
    void testCarLoadingFromDatabase () {
        clearAndLoadTestData();
        assertEquals(
                new CarInfoType(1, "Audi A3", 5.8, FuelType.BENZOL),
                dataFetcher.getCar(1)
        );
    }

    @Test
    @Order(4)
    void testAllCarsLoadingFromDatabase () {
        List<CarInfoType> expected = Arrays.asList(
                new CarInfoType(1, "Audi A3", 5.8, FuelType.BENZOL),
                new CarInfoType(2, "VW Golf 8 TDI", 4.8, FuelType.DIESEL),
                new CarInfoType(3, "Skoda Octavia IV", 4.0, FuelType.DIESEL)
        );
        assertEquals(expected, dataFetcher.getAllCars());
    }

    private void clearAndLoadTestData () {
        if (carRepository.count() == 0) {
            carRepository.saveAll(
                    Arrays.asList(
                            new Car(-1, "Audi A3", 5.8, FuelType.BENZOL),
                            new Car(-1, "VW Golf 8 TDI", 4.8, FuelType.DIESEL),
                            new Car(-1, "Skoda Octavia IV", 4.0, FuelType.DIESEL)
                    )
            );
        }
    }

    private String getTankerkoenigResponse () {
        return "{\n" +
               "    \"ok\": true,\n" +
               "    \"license\": \"CC BY 4.0 -  https:\\/\\/creativecommons.tankerkoenig.de\",\n" +
               "    \"data\": \"MTS-K\",\n" +
               "    \"prices\": {\n" +
               "        \"" + stationId + "\": {\n" +
               "            \"status\": \"open\",\n" +
               "            \"e5\": 1.439,\n" +
               "            \"e10\": false,\n" +
               "            \"diesel\": 1.219\n" +
               "        }\n" +
               "    }\n" +
               "}";
    }
}
