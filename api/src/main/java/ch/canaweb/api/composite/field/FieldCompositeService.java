package ch.canaweb.api.composite.field;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FieldCompositeService {
    @GetMapping(
            value ="/field-composite/fields",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    List<Field> getAllFields();

    @GetMapping(
            value ="/field-composite/payables",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    List<Payable> getAllPayables();

    @GetMapping(
            value ="/field-composite/receivables",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    List<Receivable> getAllReceivables();

    @GetMapping(
            value ="/field-composite/{fieldId}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    CompositeField getCompositeFieldByFieldId(@PathVariable int fieldId);

    @GetMapping(
            value ="/field-composite",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    List<CompositeField> getCompositeFields();

    @PatchMapping(
            value ="/field-composite",
            produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    CompositeField updateCompositeField();

    @PostMapping(
            value ="/field-composite",
            consumes = "application/json",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    CompositeField createCompositeField(@RequestBody CompositeField body);

    @DeleteMapping(
            path = "/field-composite/{fieldId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteField(@PathVariable int fieldId);

    @GetMapping(
            value ="/upstream-status",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    MicroServiceStatus getUpstreamMicroServicesStatus();
}
