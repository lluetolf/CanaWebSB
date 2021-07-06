package ch.canaweb.api.core.Receivable;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReceivableService {

    @GetMapping(
            value    = "/receivable/{receivableId}",
            produces = "application/json")
    Mono<Receivable> getReceivable(@PathVariable int receivableId);

    @GetMapping(
            value    = "/receivable",
            produces = "application/json")
    Flux<Receivable> getAllReceivables();

}
