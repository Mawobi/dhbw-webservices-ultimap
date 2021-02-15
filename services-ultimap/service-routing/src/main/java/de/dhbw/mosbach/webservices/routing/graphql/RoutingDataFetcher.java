package de.dhbw.mosbach.webservices.routing.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteType;

import java.util.Arrays;

@DgsComponent
public class RoutingDataFetcher {
    @DgsData(parentType = "Query", field = "geocode")
    public CoordinateType geocode (String name) {
        return new CoordinateType(49.490, 9.773);
    }

    @DgsData(parentType = "Query", field = "route")
    public RouteType getRoute (CoordinateInput start, CoordinateInput finish) {
        return new RouteType(
                60,
                50000,
                Arrays.asList(
                        new CoordinateType(49.352, 9.146),
                        new CoordinateType(49.490, 9.773)
                )
        );
    }
}
