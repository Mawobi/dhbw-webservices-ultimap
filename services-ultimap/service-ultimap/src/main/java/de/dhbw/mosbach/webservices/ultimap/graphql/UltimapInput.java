package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UltimapInput {
    private GeoInput geopoints;
    private int departure;
    private FuelInput fuel;
}
