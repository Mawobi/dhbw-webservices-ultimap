package de.dhbw.mosbach.webservices.routing.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

@Service
public class QueryType implements GraphQLQueryResolver {
    public CoordinateType geocode (String name) {
        return new CoordinateType(9.773, 49.490);
    }

    public RouteType getRoute (CoordinateInput start, CoordinateInput finish) {
        return new RouteType();
    }
}
