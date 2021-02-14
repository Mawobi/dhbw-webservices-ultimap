package de.dhbw.mosbach.webservices.ultimap.external.carinfo;


import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.FuelType;

import java.util.List;

public interface ICarinfoProvider {
    CarInfoType getCar(int carId);
    FuelPriceType getFuel(FuelType typ);
    List<CarInfoType> getAllCars();
}
