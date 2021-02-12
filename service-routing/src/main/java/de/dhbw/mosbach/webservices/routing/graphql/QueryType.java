package de.dhbw.mosbach.webservices.routing.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

public class QueryType implements GraphQLQueryResolver {
    public CoordinateType geocode (String name) {
        return new CoordinateType(9.773, 49.490);
    }

    public RouteType getRoute (CoordinateInput start, CoordinateInput finish) {
        return new RouteType();
    }
}
