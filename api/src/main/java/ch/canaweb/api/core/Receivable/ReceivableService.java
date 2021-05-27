package ch.canaweb.api.core.Receivable;

import ch.canaweb.api.core.Receivable.Receivable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReceivableService {

    /**
     * Sample usage: curl $HOST:$PORT/receivable/1
     *
     * @param receivableId
     * @return the receivable, if found, else null
     */
    @GetMapping(
            value    = "/receivable/{receivableId}",
            produces = "application/json")
    Receivable getReceivable(@PathVariable int receivableId);

    /**
     * Sample usage: curl $HOST:$PORT/receivable
     *
     * @return all receivables
     */
    @GetMapping(
            value    = "/receivable",
            produces = "application/json")
    Receivable getAllReceivables();
}
