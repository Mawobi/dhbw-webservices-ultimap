package de.dhbw.mosbach.webservices.weather.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherType;
import de.dhbw.mosbach.webservices.weather.external.response.OpenWeatherMapSimplifiedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Service
public class DefaultWeatherProvider implements IWeatherProvider {
    private final RestTemplate restTemplate;

    @Value("${ultimap.weather.token}")
    private String apiToken;

    @Value("${ultimap.weather.cache.validity}")
    private int cacheValidFor;

    private CachedWeatherData cache;

    public DefaultWeatherProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cache = new CachedWeatherData();
    }

    @Override
    public WeatherType getWeather(CoordinateInput coordinate, int timestamp) {

        if (!apiToken.equals("DUMMY")) {

            // Refresh cache if necessary
            long currentTimestamp = Instant.now().getEpochSecond();
            if (currentTimestamp > cache.getTimestamp() + cacheValidFor) {
                refreshData(coordinate, timestamp);
                log.info("Refreshed FuelPrice Cache. New Value: {}", cache);
            } else {
                log.debug("Serving fuel-price from cache. Next refresh after {}", cache.getTimestamp() + cacheValidFor);
            }
        }

        WeatherType weatherType = new WeatherType();
        weatherType.setTemp(cache.getTemp());  // Average Data of 2020
        weatherType.setRain(cache.getRain());
        return weatherType;
    }

    private void refreshData(CoordinateInput coordinate, int timestamp) {
        // https://openweathermap.org/api/hourly-forecast
        String url = String.format("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&appid=%s", coordinate.getLat(), coordinate.getLon(), apiToken);
        OpenWeatherMapSimplifiedResponse response = restTemplate.getForObject(url, OpenWeatherMapSimplifiedResponse.class);
        if (response != null) {
            cache.updateCache(response.getTemp(timestamp), response.getRain(timestamp));
        } else {
            log.error("No Response from OpenWeatherMap");
        }
    }
}
