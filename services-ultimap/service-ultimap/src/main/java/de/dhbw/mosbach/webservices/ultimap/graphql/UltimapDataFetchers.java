package de.dhbw.mosbach.webservices.ultimap.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapInput;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.UltimapType;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UltimapDataFetchers {
    @Autowired
    private ICarinfoProvider carinfoProvider;

    @DgsData(parentType = "UltimapQueryType", field = "routeInfo")
    public UltimapType getRouteInfo (UltimapInput input) {
        return new UltimapType();
    }

}
