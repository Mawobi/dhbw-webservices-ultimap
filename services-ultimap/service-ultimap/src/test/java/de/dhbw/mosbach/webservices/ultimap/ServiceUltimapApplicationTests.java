package de.dhbw.mosbach.webservices.ultimap;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.RouteInfoGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.RouteInfoProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.FuelInput;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.GeoInput;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.UltimapInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarCostInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherInfoType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;

import static de.dhbw.mosbach.webservices.ultimap.client.test.types.FuelType.BENZOL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class ServiceUltimapApplicationTests {
    @Autowired
    private RestTemplate template;
    private MockRestServiceServer mockServer;

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @BeforeEach
    void setUp () {
        mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    void contextLoads () {
    }

    @Test
    void fullTest () {
        // region expected result
        UltimapType expected = new UltimapType(
                new RouteInfoType(60, 50_000, Arrays.asList(
                        new CoordinateType(49.351978, 9.145870),
                        new CoordinateType(49.490200, 9.773150)
                )),
                new CarCostInfoType(4.2, 5.557, 3.0),
                new WeatherInfoType(-5.0, 4.2, 0.89, 0.6)
        );
        //endregion

        //region expected requests
        expectGraphQlRequest("routing", "geocode");
        expectGraphQlRequest("routing","route");
        expectGraphQlRequest("carinfo", "fuel");
        for (int i = 0 ; i < 10 ; i++) {
            expectGraphQlRequest("weather","weather" + i);
        }
        //endregion

        // region execute request
        UltimapInput input = new UltimapInput(
                new GeoInput("DHBW Mosbach", "(49.490200,9.773150)"),
                ((int) Instant.now().getEpochSecond()),
                new FuelInput(8.3, BENZOL));

        // Query everything from the endpoint
        GraphQLQueryRequest query = new GraphQLQueryRequest(
                RouteInfoGraphQLQuery.newRequest().input(input).build(),
                new RouteInfoProjectionRoot()
                        .costs().fuelCosts().wearFlatrate().totalConsumption().parent()
                        .route().distance().duration().waypoints().
                                lat().lon().root()
                        .weather().min().max().avg().rain()
        );

        UltimapType received = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.routeInfo",
                UltimapType.class);
        //endregion

        assertEquals(expected, received);
    }

    private void expectGraphQlRequest (String serviceName, String responseTemplate) {
        mockServer
                .expect(requestTo("http://ultimap-" + serviceName + ":8080/graphql"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString()))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess(loadJsonDocument(responseTemplate), MediaType.APPLICATION_JSON));
    }

    private String loadJsonDocument (String name) {
        try {
            InputStream jsonDocument = this.getClass().getClassLoader().getResourceAsStream("testdata/jsondocs/" + name + ".json");
            assert jsonDocument != null;
            return new String(jsonDocument.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
