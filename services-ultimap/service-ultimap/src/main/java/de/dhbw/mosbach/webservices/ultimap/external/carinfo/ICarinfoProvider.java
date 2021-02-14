package de.dhbw.mosbach.webservices.ultimap.external.carinfo;


import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;

import java.util.List;

public interface ICarinfoProvider {
    CarInfoType getCar(int carId);
    de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelPriceType getFuel(FuelType typ);
    List<CarInfoType> getAllCars();
}
