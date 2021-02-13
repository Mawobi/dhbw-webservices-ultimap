package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UltimapType {
    private RouteInfoType route = new RouteInfoType();
    private CarCostInfoType costs = new CarCostInfoType();
    private WeatherInfoType weather = new WeatherInfoType();
}
