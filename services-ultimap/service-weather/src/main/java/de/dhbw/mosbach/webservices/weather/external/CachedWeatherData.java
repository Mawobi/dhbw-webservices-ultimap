package de.dhbw.mosbach.webservices.weather.external;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Data
@Slf4j
public class CachedWeatherData {

    private long timestamp;
    private double temp;
    private double rain;

    public CachedWeatherData() {
        this.timestamp = 0;
        this.temp = 10.4;
        this.rain = 0;
    }

    public void updateCache(double temp, double rain) {
        this.timestamp = Instant.now().getEpochSecond();
        this.temp = temp;
        this.rain = rain;
    }
}
