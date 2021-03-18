package de.dhbw.mosbach.webservices.routing.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.routing.external.IRoutingProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteType;

import java.util.Arrays;

@DgsComponent
public class RoutingDataFetcher {

    private final IRoutingProvider routingProvider;

    public RoutingDataFetcher(IRoutingProvider routingProvider) {
        this.routingProvider = routingProvider;
    }

    @DgsData(parentType = "Query", field = "geocode")
    public CoordinateType geocode(String name) {
        return routingProvider.getGeocode(name);
    }

    @DgsData(parentType = "Query", field = "route")
    public RouteType getRoute(CoordinateInput start, CoordinateInput destination) {
        return routingProvider.getRoute(start, destination);
    }
}
