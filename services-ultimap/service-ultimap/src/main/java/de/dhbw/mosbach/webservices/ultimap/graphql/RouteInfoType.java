package de.dhbw.mosbach.webservices.ultimap.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfoType {
    private int duration = 40;
    private int distance = 14;
    private List<CoordinateType> waypoints = Arrays.asList(
            new CoordinateType(9.146, 49.352),
            new CoordinateType(9.773, 49.490)
    );
}
