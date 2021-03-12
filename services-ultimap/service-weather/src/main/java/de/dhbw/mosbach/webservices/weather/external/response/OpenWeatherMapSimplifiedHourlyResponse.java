package de.dhbw.mosbach.webservices.weather.external.response;

import lombok.Data;

@Data
public class OpenWeatherMapSimplifiedHourlyResponse {

    private long dt;
    private double temp;
    private OpenWeatherMapSimplifiedRainResponse rain;
}
