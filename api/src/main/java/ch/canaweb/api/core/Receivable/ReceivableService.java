package ch.canaweb.api.core.Receivable;

import ch.canaweb.api.core.Receivable.Receivable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReceivableService {

    @GetMapping(
            value    = "/receivable/{receivableId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Receivable> getReceivable(@PathVariable int receivableId);

    @GetMapping(
            value    = "/receivable/field/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Receivable> getReceivableForField(@PathVariable int fieldId);

    @DeleteMapping(
            value    = "/receivable/field/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteReceivableForField(@PathVariable int fieldId);

    @GetMapping(
            value    = "/receivable",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Receivable> getAllReceivables();

    @PostMapping(
            path = "/receivable",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Receivable> createReceivable(@RequestBody Receivable body);

    @PatchMapping(
            path = "/receivable",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Receivable> updateReceivable(@RequestBody Receivable body);

    @DeleteMapping(
            path = "/receivable/{receivableId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteReceivable(@PathVariable int receivableId);
}
