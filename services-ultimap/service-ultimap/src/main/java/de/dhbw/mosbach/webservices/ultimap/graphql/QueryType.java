package de.dhbw.mosbach.webservices.ultimap.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QueryType implements GraphQLQueryResolver {
    public UltimapType getRouteInfo (UltimapInput input) {
        return new UltimapType();
    }

    public CarInfoType getCarInfo (int carId) {
        return new CarInfoType();
    }

    public List<CarInfoType> getCarModels () {
        return Collections.singletonList(new CarInfoType());
    }
}
