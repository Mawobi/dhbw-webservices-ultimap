package de.dhbw.mosbach.webservices.weather.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class QueryType implements GraphQLQueryResolver {
    public WeatherType getWeather (CoordinateInput coordinate) {
        return new WeatherType(1.2, 0.8);
    }
}
