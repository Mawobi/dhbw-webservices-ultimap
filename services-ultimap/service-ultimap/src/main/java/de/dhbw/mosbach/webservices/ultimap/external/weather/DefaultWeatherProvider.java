package de.dhbw.mosbach.webservices.ultimap.external.weather;

import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.ultimap.client.weather.client.WeatherGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.weather.client.WeatherProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.weather.types.WeatherType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.util.ConversionUtilKt;
import de.dhbw.mosbach.webservices.ultimap.util.GraphQLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultWeatherProvider implements IWeatherProvider {
    private final String weatherUrl;
    private final GraphQLHelper helper;

    @Autowired
    public DefaultWeatherProvider (@Value("${services.weather.location}") String weatherLocation, GraphQLHelper helper) {
        this.weatherUrl = weatherLocation + "/graphql";
        this.helper = helper;
    }

    @Override
    public WeatherType getWeather (CoordinateType inputCoordinate, int timestamp) {
        GraphQLQueryRequest geocodeServiceRequest =
                new GraphQLQueryRequest(
                        WeatherGraphQLQuery
                                .newRequest()
                                .coordinate(ConversionUtilKt.toWeatherInput(inputCoordinate))
                                .timestamp(timestamp)
                                .build(),
                        new WeatherProjectionRoot()
                                .rain()
                                .temp()
                );
        return helper.request(
                weatherUrl,
                geocodeServiceRequest,
                "weather",
                WeatherType.class);

    }
}
