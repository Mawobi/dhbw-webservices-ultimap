package de.dhbw.mosbach.webservices.weather.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.WeatherType;

@DgsComponent
public class WeatherDataFetcher {
    @DgsData(parentType = "WeatherQueryType", field = "weather")
    public WeatherType getWeather (CoordinateInput coordinate) {
        return new WeatherType(1.2, 0.8);
    }
}
