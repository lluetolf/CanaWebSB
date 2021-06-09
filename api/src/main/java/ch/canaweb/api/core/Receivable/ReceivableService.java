package ch.canaweb.api.core.Receivable;

import org.springframework.web.bind.annotation.*;

public interface ReceivableService {

    @GetMapping(
            value    = "/receivable/{receivableId}",
            produces = "application/json")
    Receivable getReceivable(@PathVariable int receivableId);

    @GetMapping(
            value    = "/receivable",
            produces = "application/json")
    Receivable getAllReceivables();

}
