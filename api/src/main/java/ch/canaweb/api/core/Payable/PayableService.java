package ch.canaweb.api.core.Payable;

import org.springframework.web.bind.annotation.*;

public interface PayableService {

    @GetMapping(
            value    = "/payable/{payableId}",
            produces = "application/json")
    Payable getPayable(@PathVariable int payableId);

    @GetMapping(
            value    = "/payable",
            produces = "application/json")
    Payable getAllPayables();
}
