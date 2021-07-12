package ch.canaweb.api.composite.field;

import ch.canaweb.api.core.Field.Field;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FieldCompositeService {
    @GetMapping(
            value ="/field-composite/fields",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Field> getAllFields();


    @GetMapping(
            value ="/field-composite/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<CompositeField> getCompositeField(@PathVariable int fieldId);

    @GetMapping(
            value ="/field-composite",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<CompositeField> getCompositeFields();

    @PatchMapping(
            value ="/field-composite",
            produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> updateCompositeField();

    @PostMapping(
            value ="/field-composite",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    CompositeField createCompositeField(@RequestBody CompositeField body);

    @GetMapping(
            value ="/upstream-status",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<MicroServiceStatus> getUpstreamMicroServicesStatus();

    @DeleteMapping(
            path = "/field-composite/{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteField(@PathVariable int fieldId);
}
