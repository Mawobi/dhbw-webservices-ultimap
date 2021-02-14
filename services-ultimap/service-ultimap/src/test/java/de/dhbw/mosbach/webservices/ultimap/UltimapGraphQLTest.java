package de.dhbw.mosbach.webservices.ultimap;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarInfoGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarInfoProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarModelsGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarModelsProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.RouteInfoGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.RouteInfoProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.FuelInput;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.GeoInput;
import de.dhbw.mosbach.webservices.ultimap.client.test.types.UltimapInput;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.DefaultCarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.CarinfoDataFetcher;
import de.dhbw.mosbach.webservices.ultimap.graphql.UltimapDataFetcher;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarCostInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherInfoType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static de.dhbw.mosbach.webservices.ultimap.client.test.types.FuelType.BENZOL;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Only load the classes that sould be tested
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        CarinfoDataFetcher.class,
        UltimapDataFetcher.class,
        TestConfiguration.class
})
public class UltimapGraphQLTest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void loadsAllCars () {
        List<CarInfoType> expected = Arrays.asList(
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

        GraphQLQueryRequest query = new GraphQLQueryRequest(
                CarModelsGraphQLQuery.newRequest().build(),
                new CarModelsProjectionRoot()
                        .name()
                        .consumption()
                        .id()
                        .typ());
        List<CarInfoType> carListResult = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.carModels[*]",
                DefaultCarinfoProvider.CarInfoList.class);

        assertEquals(expected, carListResult);
    }

    @Test
    void loadSpecificCarInfo () {
        CarInfoType expected = new CarInfoType(1, "BMW 3er M", 8.7, FuelType.DIESEL);

        GraphQLQueryRequest query = new GraphQLQueryRequest(
                CarInfoGraphQLQuery.newRequest().carId(1).build(),
                new CarInfoProjectionRoot()
                        .name()
                        .consumption()
                        .id()
                        .typ());
        CarInfoType carListResult = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.carInfo",
                CarInfoType.class);

        assertEquals(expected, carListResult);
    }

    @Test
    void loadRoutingInformation () {
        UltimapType expected = UltimapType
                .newBuilder()
                .route(RouteInfoType.newBuilder()
                                    .duration(60)
                                    .distance(50_000)
                                    .waypoints(Arrays.asList(
                                            new CoordinateType(49.351978, 9.145870),
                                            new CoordinateType(49.490200, 9.773150)
                                    ))
                                    .build())
                .costs(CarCostInfoType
                               .newBuilder()
                               .totalConsumption(4150.0)
                               .fuelCosts(4150 * 1.439 /*consumption x fuelCost per litre*/)
                               .wearFlatrate(3000.0)
                               .build())
                .weather(WeatherInfoType
                                 .newBuilder()
                                 .min(12.4)
                                 .avg(12.4)
                                 .max(12.4)
                                 .rain(1.0)
                                 .build())
                .build();

        UltimapInput input = UltimapInput.newBuilder()
                                         .departure(0) // Not relevant for mocked data
                                         .fuel(
                                                 FuelInput
                                                         .newBuilder()
                                                         .consumption(8.3)
                                                         .typ(BENZOL)
                                                         .build()
                                         )
                                         .geopoints(
                                                 GeoInput
                                                         .newBuilder()
                                                         .start("DHBW Mosbach")
                                                         .destination("DHBW Bad Mergentheim")
                                                         .build()
                                         )
                                         .build();

        GraphQLQueryRequest query = new GraphQLQueryRequest(
                RouteInfoGraphQLQuery.newRequest().input(input).build(),
                new RouteInfoProjectionRoot()
                        .costs().fuelCosts().wearFlatrate().totalConsumption().parent()
                        .route().distance().duration().waypoints().
                                lat().lon().root()
                        .weather().min().max().avg().rain()
        );
        UltimapType result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.routeInfo",
                UltimapType.class);

        assertEquals(expected, result);

    }
}
