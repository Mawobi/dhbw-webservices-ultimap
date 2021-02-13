package de.dhbw.mosbach.webservices.routing.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteType {
    private int time = 60;
    private int distance = 50000;
    private List<CoordinateType> waypoints = Arrays.asList(
            new CoordinateType(9.146, 49.352),
            new CoordinateType(9.773, 49.490)
    );
}
