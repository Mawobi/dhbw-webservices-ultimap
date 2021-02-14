package de.dhbw.mosbach.webservices.ultimap.external.routing;


import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.CoordinateType;
import de.dhbw.mosbach.webservices.ultimap.graphql.client.types.RouteType;

public interface IRoutingProvider {
    CoordinateType geocode(String name);
    RouteType getRoute(CoordinateType start, CoordinateType finish);
}
