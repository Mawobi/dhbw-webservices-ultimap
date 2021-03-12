package de.dhbw.mosbach.webservices.weather.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenWeatherMapSimplifiedRainResponse {

    @JsonProperty("1h")
    private double oneH;
}
