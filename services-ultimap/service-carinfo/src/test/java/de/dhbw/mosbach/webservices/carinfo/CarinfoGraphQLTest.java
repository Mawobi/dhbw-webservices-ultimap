package de.dhbw.mosbach.webservices.carinfo;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.carinfo.data.ICarRepository;
import de.dhbw.mosbach.webservices.carinfo.external.IFuelPriceProvider;
import de.dhbw.mosbach.webservices.carinfo.graphql.CarInfoDataFetcher;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.AllCarsGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.AllCarsProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.CarProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.FuelGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.test.client.FuelProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.dhbw.mosbach.webservices.ultimap.client.test.types.FuelType.BENZOL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        CarInfoDataFetcher.class
})
public class CarinfoGraphQLTest {
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
                AllCarsGraphQLQuery.newRequest().build(),
                new AllCarsProjectionRoot()
                        .name()
                        .consumption()
                        .id()
                        .typ());
        List<CarInfoType> carListResult = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.allCars[*]",
                CarInfoList.class);

        assertEquals(expected, carListResult);
    }

    @Test
    void loadSpecificCarInfo () {
        CarInfoType expected = new CarInfoType(1, "BMW 3er M", 8.7, FuelType.DIESEL);

        GraphQLQueryRequest query = new GraphQLQueryRequest(
                CarGraphQLQuery.newRequest().carId(1).build(),
                new CarProjectionRoot()
                        .name()
                        .consumption()
                        .id()
                        .typ());
        CarInfoType carListResult = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.car",
                CarInfoType.class);

        assertEquals(expected, carListResult);
    }

    @Test
    void loadFuel () {
        FuelPriceType expected = new FuelPriceType(1.449);

        GraphQLQueryRequest query = new GraphQLQueryRequest(
                FuelGraphQLQuery.newRequest().typ(BENZOL).build(),
                new FuelProjectionRoot()
                        .price());
        FuelPriceType result = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query.serialize(),
                "data.fuel",
                FuelPriceType.class);

        assertEquals(expected, result);
    }

    @Bean
    public IFuelPriceProvider getMockFuelPriceProvider () {
        return new MockObjects.MockFuelPriceProvider();
    }

    @Bean
    public ICarRepository getMockCarRepository () {
        return new MockObjects.MockCarRepository();
    }

    public static class CarInfoList extends ArrayList<CarInfoType> {
    }
}
