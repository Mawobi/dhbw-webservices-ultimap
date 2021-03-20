package de.dhbw.mosbach.webservices.weather.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class CachedWeatherData {

    @Value("${ultimap.weather.cache.validity}")
    private int cacheValidFor;
    private Map<CoordinateInput, CacheData> data;

    public CachedWeatherData() {
        this.data = new HashMap<>();
    }

    public CacheData getCachedData(CoordinateInput coordinateInput) {

        CacheData returnCache = new CacheData();

        for (Map.Entry<CoordinateInput, CacheData> cacheDataEntry : data.entrySet()) {

            if (Instant.now().getEpochSecond() < cacheDataEntry.getValue().getTimestamp() + cacheValidFor) {
                if (distance(coordinateInput.getLat(), coordinateInput.getLon(), cacheDataEntry.getKey().getLat(), cacheDataEntry.getKey().getLon()) < 10) {
                    if (returnCache.getTimestamp() < cacheDataEntry.getValue().getTimestamp()) {
                        returnCache = cacheDataEntry.getValue();
                    }
                }
            } else {
                data.remove(cacheDataEntry.getKey());
            }
        }

        return returnCache;
    }

    public void addCache(CoordinateInput coordinateInput, CacheData cacheData) {
        data.put(coordinateInput, cacheData);
    }

    // Returns distance in kilometers
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
