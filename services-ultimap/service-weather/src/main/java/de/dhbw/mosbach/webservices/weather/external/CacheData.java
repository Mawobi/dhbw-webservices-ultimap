package de.dhbw.mosbach.webservices.weather.external;

import lombok.Data;

@Data
public class CacheData {

    private long timestamp;
    private double temp;
    private double rain;

    public CacheData() {
        this.timestamp = 0;
        this.temp = 10.4;
        this.rain = 0;
    }
}
