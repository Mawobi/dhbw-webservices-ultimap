package de.dhbw.mosbach.webservices.ultimap.external.routing;


import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType;

public interface IRoutingProvider {
    CoordinateType geocode(String name);
    de.dhbw.mosbach.webservices.ultimap.client.routing.types.RouteType getRoute(CoordinateType start, CoordinateType finish);
}
