package de.dhbw.mosbach.webservices.carinfo.graphql;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;

import java.util.Collections;
import java.util.List;

@DgsComponent
public class CarInfoDataFetcher {
    @DgsData(parentType = "Query", field = "car")
    public CarInfoType getCar (int carId) {
        return new CarInfoType(
                0,
                "Audi A3",
                6.9,
                FuelType.BENZOL
        );
    }

    @DgsData(parentType = "Query", field = "fuel")
    public FuelPriceType getFuel (FuelType typ) {
        return new FuelPriceType(1.449);
    }

    @DgsData(parentType = "Query", field = "allCars")
    public List<CarInfoType> getAllCars () {
        return Collections.singletonList(new CarInfoType(
                0,
                "Audi A3",
                6.9,
                FuelType.BENZOL
        ));
    }
}
