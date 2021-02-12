package de.dhbw.mosbach.webservices.carinfo.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QueryType implements GraphQLQueryResolver {
    public CarInfoType getCarInfo (int carId) {
        return new CarInfoType();
    }

    public FuelPriceType getFuelInfo (FuelType typ) {
        return new FuelPriceType();
    }

    public List<CarInfoType> getAllModels () {
        return Collections.singletonList(new CarInfoType());
    }
}
