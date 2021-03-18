package de.dhbw.mosbach.webservices.routing.external;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.RouteType;

public interface IRoutingProvider {
    RouteType getRoute(CoordinateInput start, CoordinateInput destination);
    CoordinateType getGeocode(String name);
}
