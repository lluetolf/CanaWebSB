package ch.canaweb.api.core.Payable;

import ch.canaweb.api.core.Field.Field;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PayableService {

    /**
     * Sample usage: curl $HOST:$PORT/payable/1
     *
     * @param payableId
     * @return the field, if found, else null
     */
    @GetMapping(
            value    = "/payable/{payableId}",
            produces = "application/json")
    Field getPayable(@PathVariable int payableId);

    /**
     * Sample usage: curl $HOST:$PORT/payable
     *
     * @return all payables
     */
    @GetMapping(
            value    = "/payable",
            produces = "application/json")
    Field getAllPayables();
}
