package de.dhbw.mosbach.webservices.routing.external.routeresponse;

import lombok.Data;

@Data
public class OpenRouteServiceSimplifiedRFeatureResponse {

    private OpenRouteServiceSimplifiedRPropertyResponse properties;
    private OpenRouteServiceSimplifiedRGeometryResponse geometry;
}
