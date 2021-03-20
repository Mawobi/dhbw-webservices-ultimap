package de.dhbw.mosbach.webservices.carinfo;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import de.dhbw.mosbach.webservices.carinfo.data.Car;
import de.dhbw.mosbach.webservices.carinfo.data.ICarRepository;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceCarinfoApplicationTests {
    @Autowired
    private RestTemplate template;
    private MockRestServiceServer mockServer;

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    @Autowired
    private ICarRepository carRepository;

    @Value("${ultimap.carinfo.fuel.station}")
    private String stationId;
    @Value("${ultimap.carinfo.token}")
    private String apiToken;

    @BeforeEach
    void setUp () {
        mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    void contextLoads () {
    }

    @Test
    void fullTest () {
        clearAndLoadTestData();
        String url = String.format("https://creativecommons.tankerkoenig.de/json/prices.php?ids=%s&apikey=%s", stationId, apiToken);
        mockServer
                .expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTankerkoenigResponse(), MediaType.APPLICATION_JSON));

        String query = "{\n" +
                       "  car(carId: 1) {\n" +
                       "    id\n" +
                       "    name\n" +
                       "    consumption\n" +
                       "    typ\n" +
                       "  }\n" +
                       "  allCars {\n" +
                       "    id\n" +
                       "    name\n" +
                       "    consumption\n" +
                       "    typ\n" +
                       "  }\n" +
                       "  fuel(typ: DIESEL) {\n" +
                       "    price\n" +
                       "  }\n" +
                       "}";
        TestData actual = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data",
                TestData.class);

        assertEquals(new TestData(), actual);
    }

    @Data
    static class TestData {
        private CarInfoType car = new CarInfoType(1, "Audi A3", 5.8, FuelType.BENZOL);
        private List<CarInfoType> allCars = Arrays.asList(
                new CarInfoType(1, "Audi A3", 5.8, FuelType.BENZOL),
                new CarInfoType(2, "VW Golf 8 TDI", 4.8, FuelType.DIESEL),
                new CarInfoType(3, "Skoda Octavia IV", 4.0, FuelType.DIESEL)
        );
        private FuelPriceType fuel = new FuelPriceType(1.219);
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
