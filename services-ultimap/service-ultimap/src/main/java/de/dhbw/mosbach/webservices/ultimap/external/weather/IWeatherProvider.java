package de.dhbw.mosbach.webservices.ultimap.external.weather;


import de.dhbw.mosbach.webservices.ultimap.client.weather.types.WeatherType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;

public interface IWeatherProvider {
    WeatherType getWeather (CoordinateType inputCoordinate, int timestamp);
}
