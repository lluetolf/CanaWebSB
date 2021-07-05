package ch.canaweb.api.core.Payable;

import ch.canaweb.api.core.Field.Field;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PayableService {

    @GetMapping(
            value    = "/payable/{payableId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Payable> getPayable(@PathVariable int payableId);

    @GetMapping(
            value    = "/payable/field/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayablesForField(@PathVariable int fieldId);

    @GetMapping(
            value    = "/payable",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayables();

    @PostMapping(
            path = "/payable",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Payable> createPayable(@RequestBody Payable body);

    @PutMapping(
            path = "/payable",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Payable> updatePayable(@RequestBody Payable body);

    @DeleteMapping(
            path = "/payable/{payableId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deletePayable(@PathVariable int payableId);
}
