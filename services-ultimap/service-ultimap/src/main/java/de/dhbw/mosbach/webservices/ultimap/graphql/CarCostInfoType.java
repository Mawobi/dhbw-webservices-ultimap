package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCostInfoType {
    private double totalConsumption = 10.5;
    private double fuelCosts = 84.57;
    private double wearFlatrate = 14.14;
}
