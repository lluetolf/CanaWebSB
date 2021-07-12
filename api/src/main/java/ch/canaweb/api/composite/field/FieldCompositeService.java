package ch.canaweb.api.composite.field;

import ch.canaweb.api.composite.field.CompositeField;
import ch.canaweb.api.composite.field.MicroServiceStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.*;

public interface FieldCompositeService {
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

    @GetMapping(
            value ="/upstream-status",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    Mono<MicroServiceStatus> getUpstreamMicroServicesStatus();

}
