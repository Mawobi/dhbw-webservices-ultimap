package de.dhbw.mosbach.webservices.carinfo.external;

import lombok.Data;

import java.util.HashMap;

@Data
public class TankerkoenigSimplifiedResponse {
    private boolean ok;
    private String message = "";
    private HashMap<String, CachedPriceData> prices = new HashMap<>();
}
