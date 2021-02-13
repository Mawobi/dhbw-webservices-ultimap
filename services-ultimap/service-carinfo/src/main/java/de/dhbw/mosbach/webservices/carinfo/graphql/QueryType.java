package de.dhbw.mosbach.webservices.carinfo.graphql;


import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QueryType implements GraphQLQueryResolver {
    public CarInfoType getCar (int carId) {
        return new CarInfoType();
    }

    public FuelPriceType getFuel (FuelType typ) {
        return new FuelPriceType();
    }

    public List<CarInfoType> getAllCars () {
        return Collections.singletonList(new CarInfoType());
    }
}
