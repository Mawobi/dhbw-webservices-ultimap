package de.dhbw.mosbach.webservices.carinfo.graphql;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.carinfo.data.Car;
import de.dhbw.mosbach.webservices.carinfo.data.ICarRepository;
import de.dhbw.mosbach.webservices.carinfo.external.IFuelPriceProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DgsComponent
public class CarInfoDataFetcher {
    private final ICarRepository carRepository;
    private final IFuelPriceProvider fuelPriceProvider;

    public CarInfoDataFetcher (ICarRepository carRepository, IFuelPriceProvider fuelPriceProvider) {
        this.carRepository = carRepository;
        this.fuelPriceProvider = fuelPriceProvider;
    }

    @DgsData(parentType = "Query", field = "car")
    public CarInfoType getCar (int carId) {
        return carRepository.findById(carId).orElseThrow().toCarInfoType();
    }

    @DgsData(parentType = "Query", field = "fuel")
    public FuelPriceType getFuel (FuelType typ) {
        return new FuelPriceType(fuelPriceProvider.getFuelPrice(typ));
    }

    @DgsData(parentType = "Query", field = "allCars")
    public List<CarInfoType> getAllCars () {
        return StreamSupport
                .stream(carRepository.findAll().spliterator(), false)
                .map(Car::toCarInfoType)
                .collect(Collectors.toList());
    }
}
