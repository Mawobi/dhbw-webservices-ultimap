package de.dhbw.mosbach.webservices.ultimap.external.carinfo;

import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.AllCarsGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.AllCarsProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.CarGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.CarProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.FuelGraphQLQuery;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.client.FuelProjectionRoot;
import de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelPriceType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import de.dhbw.mosbach.webservices.ultimap.util.GraphQLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultCarinfoProvider implements ICarinfoProvider {

    private final String carinfoUrl;
    private final GraphQLHelper helper;

    @Autowired
    public DefaultCarinfoProvider (@Value("${services.carinfo.location}") String carinfoLocation, GraphQLHelper helper) {
        this.carinfoUrl = carinfoLocation + "/graphql";
        this.helper = helper;
    }

    @Override
    public CarInfoType getCar (int carId) {
        GraphQLQueryRequest carinfoServiceRequest =
                new GraphQLQueryRequest(
                        CarGraphQLQuery
                                .newRequest()
                                .carId(carId)
                                .build(),
                        new CarProjectionRoot().id().name().consumption().typ()
                );
        return helper.request(
                carinfoUrl,
                carinfoServiceRequest,
                "car",
                CarInfoType.class);
    }

    @Override
    public FuelPriceType getFuel (FuelType typ) {
        GraphQLQueryRequest carinfoServiceRequest =
                new GraphQLQueryRequest(
                        FuelGraphQLQuery
                                .newRequest()
                                .typ(de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelType.valueOf(typ.name()))
                                .build(),
                        new FuelProjectionRoot().price()
                );

        return helper.request(
                carinfoUrl,
                carinfoServiceRequest,
                "fuel",
                FuelPriceType.class);
    }

    @Override
    public List<CarInfoType> getAllCars () {
        GraphQLQueryRequest carinfoServiceRequest =
                new GraphQLQueryRequest(
                        AllCarsGraphQLQuery.newRequest().build(),
                        new AllCarsProjectionRoot().id().name().consumption().typ()
                );

        return helper.request(
                carinfoUrl,
                carinfoServiceRequest,
                "allCars",
                CarInfoList.class);
    }

    // Needed to invoke generic method
    public static class CarInfoList extends ArrayList<CarInfoType> { }
}
