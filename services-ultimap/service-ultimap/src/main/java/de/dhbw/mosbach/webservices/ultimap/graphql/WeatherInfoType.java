package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherInfoType {
    private double min = -1;
    private double max = 5;
    private double avg = 3;

    private double rain = 0.4;
}
