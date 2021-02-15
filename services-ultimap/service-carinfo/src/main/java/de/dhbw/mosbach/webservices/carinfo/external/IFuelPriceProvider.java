package de.dhbw.mosbach.webservices.carinfo.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;

public interface IFuelPriceProvider {
    double getFuelPrice(FuelType type);
}
