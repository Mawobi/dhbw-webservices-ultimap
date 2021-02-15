package de.dhbw.mosbach.webservices.carinfo;

import de.dhbw.mosbach.webservices.carinfo.data.Car;
import de.dhbw.mosbach.webservices.carinfo.data.ICarRepository;
import de.dhbw.mosbach.webservices.carinfo.external.IFuelPriceProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MockObjects {
    // Mocked Price Provider
    public static class MockFuelPriceProvider implements IFuelPriceProvider {
        @Override
        public double getFuelPrice (FuelType type) {
            return 1.449;
        }
    }

    // Mocked Repository (CRUD only limited)
    public static class MockCarRepository implements ICarRepository {
        private final List<Car> cars = Arrays.asList(
                new Car(
                        0,
                        "Lamborghini Aventador S",
                        19.0,
                        FuelType.BENZOL
                ),
                new Car(
                        1,
                        "BMW 3er M",
                        8.7,
                        FuelType.DIESEL
                )
        );

        @NotNull
        @Override
        public <S extends Car> S save (@NotNull S entity) {
            return entity;
        }

        @NotNull
        @Override
        public <S extends Car> Iterable<S> saveAll (@NotNull Iterable<S> entities) {
            return entities;
        }

        @NotNull
        @Override
        public Optional<Car> findById (@NotNull Integer integer) {
            return Optional.ofNullable(cars.get(integer));
        }

        @Override
        public boolean existsById (@NotNull Integer integer) {
            return cars.size() > integer;
        }

        @NotNull
        @Override
        public Iterable<Car> findAll () {
            return cars;
        }

        @NotNull
        @Override
        public Iterable<Car> findAllById (@NotNull Iterable<Integer> integers) {
            return StreamSupport.stream(integers.spliterator(), false)
                                .map(cars::get).collect(Collectors.toList());
        }

        @Override
        public long count () {
            return cars.size();
        }

        @Override
        public void deleteById (@NotNull Integer integer) {
        }

        @Override
        public void delete (@NotNull Car entity) {
        }

        @Override
        public void deleteAll (@NotNull Iterable<? extends Car> entities) {
        }

        @Override
        public void deleteAll () {
        }
    }

}
