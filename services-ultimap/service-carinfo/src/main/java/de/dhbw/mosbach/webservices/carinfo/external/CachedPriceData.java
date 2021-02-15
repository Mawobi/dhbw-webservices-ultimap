package de.dhbw.mosbach.webservices.carinfo.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Data
@Slf4j
public class CachedPriceData {
    private long timestamp = Instant.now().getEpochSecond();
    private String status;
    private JsonNode e5;
    private JsonNode diesel;

    public CachedPriceData combine(CachedPriceData other) {
        // Replace this objects values, that are not filled
        log.debug("Reference station currently has state \"{}\"", status);

        // No price for benzol
        if(e5 == null || (e5.isBoolean() && !e5.asBoolean())) {
            if(other.getE5() == null){
                // Load average as backup
                e5 = DoubleNode.valueOf(1.432);
            } else {
                setE5(other.getE5());
            }
        }
        
        // No price for diesel
        if(diesel == null || (diesel.isBoolean() && !diesel.asBoolean())) {
            if(other.getDiesel() == null){
                // Load average as backup
                diesel = DoubleNode.valueOf(1.267);
            } else {
                setDiesel(other.getDiesel());
            }
        }

        return this;
    }
}
