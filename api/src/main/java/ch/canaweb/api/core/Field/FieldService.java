package ch.canaweb.api.core.Field;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FieldService {

    @GetMapping(
            path = "/field/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> getField(@PathVariable int fieldId);

    @GetMapping(
            path = "/field",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Flux<Field> getAllFields();

    @PostMapping(
            path = "/field",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Field> createField(@RequestBody Field body);

    @PutMapping(
            path = "/field",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<Field> updateField(@RequestBody Field body);

    @DeleteMapping(
            path = "/field/{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<Void> deleteField(@PathVariable int fieldId);
}
