package de.dhbw.mosbach.webservices.ultimap.external.carinfo;

import de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DefaultCarinfoProvider implements ICarinfoProvider {

    @Autowired
    private final RestTemplate dgsTemplate;

    private final List<CarInfoType> cars = Arrays.asList(
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

    @Override
    public CarInfoType getCar (int carId) {
        return cars.get(carId);
    }

    @Override
    public FuelPriceType getFuel (FuelType typ) {
        switch (typ) {
            case BENZOL:
                return new FuelPriceType(1.439);
            case DIESEL:
                return new FuelPriceType(1.129);
        }

        return null;
    }

    @Override
    public List<CarInfoType> getAllCars () {
        return cars;
    }
}
