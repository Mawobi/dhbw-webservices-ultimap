package de.dhbw.mosbach.webservices.carinfo.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Service
public class DefaultFuelPriceProvider implements IFuelPriceProvider {
    private final RestTemplate restTemplate;

    @Value("${ultimap.carinfo.fuel.station}")
    private String stationId;

    @Value("${ultimap.carinfo.token}")
    private String apiToken;

    @Value("${ultimap.carinfo.cache.validity}")
    private int cacheValidFor;

    private CachedPriceData cache;

    public DefaultFuelPriceProvider (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        cache = new CachedPriceData();
        cache.setTimestamp(0);
    }


    @Override
    public double getFuelPrice (FuelType type) {
        if (!apiToken.equals("DUMMY")) {

            // Refresh cache if necessary
            long currentTimestamp = Instant.now().getEpochSecond();
            if (currentTimestamp > cache.getTimestamp() + cacheValidFor) {
                refreshData();
                log.info("Refreshed FuelPrice Cache. New Value: {}", cache);
            } else {
                log.debug("Serving fuel-price from cache. Next refresh after {}", cache.getTimestamp() + cacheValidFor);
            }

            switch (type) {
                case DIESEL:
                    return cache.getDiesel().asDouble();
                case BENZOL:
                    return cache.getE5().asDouble();
            }
        }

        switch (type) {
            case BENZOL:
                return 1.432; // Average of 2019
            case DIESEL:
                return 1.267; // Average of 2019
            default:
                return 0; // Unknown FuelType
        }
    }

    public void resetCache() {
        cache.setTimestamp(0);
    }

    private void refreshData () {
        // https://creativecommons.tankerkoenig.de/
        String url = String.format("https://creativecommons.tankerkoenig.de/json/prices.php?ids=%s&apikey=%s", stationId, apiToken);
        TankerkoenigSimplifiedResponse response = restTemplate.getForObject(url, TankerkoenigSimplifiedResponse.class);
        if (response != null) {
            if (response.isOk()) {
                cache = response.getPrices().get(stationId).combine(cache);
            } else {
                log.warn("Tankerkoenig responded with an error: {}", response.getMessage());
            }
        } else {
            log.error("No Response from Tankerkoenig");
        }

        // Mark cache as refreshed anyway to prevent too many requests
        cache = new CachedPriceData().combine(cache);
    }
}
