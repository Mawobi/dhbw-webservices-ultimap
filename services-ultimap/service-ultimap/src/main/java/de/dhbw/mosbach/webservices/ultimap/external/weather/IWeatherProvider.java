package de.dhbw.mosbach.webservices.ultimap.external.weather;

import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.WeatherType;

public interface IWeatherProvider {
    WeatherType getWeather(CoordinateType coordinateType);
}
