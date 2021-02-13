package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfoType {
    private int id = -1;
    private String name = "VW Golf 6 GTI";
    private double consumption = 3.14;
    private FuelType typ = FuelType.DIESEL;
}
