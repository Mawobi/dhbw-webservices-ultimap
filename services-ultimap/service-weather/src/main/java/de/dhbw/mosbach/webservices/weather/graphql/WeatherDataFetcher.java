package de.dhbw.mosbach.webservices.weather.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherType;

@DgsComponent
public class WeatherDataFetcher {
    @DgsData(parentType = "Query", field = "weather")
    public WeatherType getWeather (CoordinateInput coordinate, int timestamp) {
        return new WeatherType(1.2, 0.8);
    }
}
