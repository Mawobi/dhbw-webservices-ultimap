package de.dhbw.mosbach.webservices.ultimap.external.routing;

import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.ultimap.client.routing.client.GeocodeGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.routing.client.GeocodeProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.routing.client.RouteGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.routing.client.RouteProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.routing.types.RouteType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.util.ConversionUtilKt;
import de.dhbw.mosbach.webservices.ultimap.util.GraphQLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoutingProvider implements IRoutingProvider {
    private final String routingUrl;
    private final GraphQLHelper helper;

    @Autowired
    public DefaultRoutingProvider (@Value("${services.routing.location}") String routingLocation, GraphQLHelper helper) {
        this.routingUrl = routingLocation + "/graphql";
        this.helper = helper;
    }

    @Override
    public CoordinateType geocode (String name) {
        GraphQLQueryRequest geocodeServiceRequest =
                new GraphQLQueryRequest(
                        GeocodeGraphQLQuery
                                .newRequest()
                                .name(name)
                                .build(),
                        new GeocodeProjectionRoot()
                                .lat()
                                .lon()
                );
        return helper.request(
                routingUrl,
                geocodeServiceRequest,
                "geocode",
                CoordinateType.class);
    }

    @Override
    public RouteType getRoute (CoordinateType start, CoordinateType destination) {
        GraphQLQueryRequest routingServiceRequest =
                new GraphQLQueryRequest(
                        RouteGraphQLQuery
                                .newRequest()
                                .start(ConversionUtilKt.toRoutingInput(start))
                                .destination(ConversionUtilKt.toRoutingInput(destination))
                                .build(),
                        new RouteProjectionRoot()
                                .distance()
                                .time()
                                .waypoints().
                                        lat().
                                        lon()
                );
        return helper.request(
                routingUrl,
                routingServiceRequest,
                "route",
                RouteType.class);
    }
}
