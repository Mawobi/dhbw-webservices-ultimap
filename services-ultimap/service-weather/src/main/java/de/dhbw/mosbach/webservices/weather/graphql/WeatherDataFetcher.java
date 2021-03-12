package de.dhbw.mosbach.webservices.weather.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherType;
import de.dhbw.mosbach.webservices.weather.external.IWeatherProvider;

@DgsComponent
public class WeatherDataFetcher {

    private final IWeatherProvider weatherProvider;

    public WeatherDataFetcher(IWeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    @DgsData(parentType = "Query", field = "weather")
    public WeatherType getWeather(CoordinateInput coordinate, int timestamp) {
        return weatherProvider.getWeather(coordinate, timestamp);
    }
}
