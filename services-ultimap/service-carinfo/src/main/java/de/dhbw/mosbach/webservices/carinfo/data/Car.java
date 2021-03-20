package de.dhbw.mosbach.webservices.carinfo.data;

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType;
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private Double consumption;
    private FuelType fueltype;

    public CarInfoType toCarInfoType () {
        return CarInfoType
                .newBuilder()
                .id(getId())
                .name(getName())
                .consumption(getConsumption())
                .typ(getFueltype())
                .build();
    }
}
