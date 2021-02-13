package de.dhbw.mosbach.webservices.weather.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateInput {
    private double lon;
    private double lat;
}
