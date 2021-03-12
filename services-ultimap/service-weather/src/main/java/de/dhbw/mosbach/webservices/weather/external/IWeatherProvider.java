package de.dhbw.mosbach.webservices.weather.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.WeatherType;

public interface IWeatherProvider {

    WeatherType getWeather(CoordinateInput coordinate, int timestamp);
}
