package de.dhbw.mosbach.webservices.routing.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.RouteType;

import java.util.Arrays;

@DgsComponent
public class RoutingDataFetcher {
    @DgsData(parentType = "RoutingQueryType", field = "geocode")
    public CoordinateType geocode (String name) {
        return new CoordinateType(9.773, 49.490);
    }

    @DgsData(parentType = "RoutingQueryType", field = "route")
    public RouteType getRoute (CoordinateInput start, CoordinateInput finish) {
        return new RouteType(
                60,
                50000,
                Arrays.asList(
                        new CoordinateType(9.146, 49.352),
                        new CoordinateType(9.773, 49.490)
                )
        );
    }
}
