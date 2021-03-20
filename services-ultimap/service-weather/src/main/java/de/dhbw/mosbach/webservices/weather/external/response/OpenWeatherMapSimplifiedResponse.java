package de.dhbw.mosbach.webservices.weather.external.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OpenWeatherMapSimplifiedResponse {

    private List<OpenWeatherMapSimplifiedHourlyResponse> hourly = new ArrayList<>();

    public double getTemp(long timestamp) {
        return getListItem(timestamp).getTemp();
    }

    public double getRain(long timestamp) {
        return getListItem(timestamp).getRain().getOneH();
    }

    private OpenWeatherMapSimplifiedHourlyResponse getListItem(long timestamp) {
        for (OpenWeatherMapSimplifiedHourlyResponse listItem : hourly) {
            if (Math.abs(listItem.getDt() - timestamp) <= 1800)
                return listItem;
        }
        return hourly.get(0);
    }
}
