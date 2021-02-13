package de.dhbw.mosbach.webservices.carinfo.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfoType {
    private int id = -1;
    private String name = "Audi A3";
    private double consumption = 5.0;
    private FuelType typ = FuelType.BENZOL;
}
