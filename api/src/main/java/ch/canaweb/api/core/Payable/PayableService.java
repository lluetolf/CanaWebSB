package ch.canaweb.api.core.Payable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

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

    @DeleteMapping(
            value    = "/payable/field/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteAllPayablesForField(@PathVariable int fieldId);

    @GetMapping(
            value    = "/payable",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayables();

    @GetMapping(
            value    = "/payable/between",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Payable> getAllPayablesBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to);

    @PostMapping(
            path = "/payable",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Payable> createPayable(@RequestBody Payable body);

    @PostMapping(
            path = "/payables",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Flux<Payable> createPayables(List<Payable> payables);

    @PatchMapping(
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
