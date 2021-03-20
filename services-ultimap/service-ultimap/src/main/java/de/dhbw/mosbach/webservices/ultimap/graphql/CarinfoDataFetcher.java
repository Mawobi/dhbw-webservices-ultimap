package de.dhbw.mosbach.webservices.ultimap.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class CarinfoDataFetcher {
    @Autowired
    private ICarinfoProvider carinfoProvider;

    @DgsData(parentType = "Query", field = "carInfo")
    public CarInfoType getCarInfo (int carId) {
        return carinfoProvider.getCar(carId);
    }

    @DgsData(parentType = "Query", field = "carModels")
    public List<CarInfoType> getCarModels () {
        return carinfoProvider.getAllCars();
    }
}
