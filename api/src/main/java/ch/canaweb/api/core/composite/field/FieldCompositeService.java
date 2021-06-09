package ch.canaweb.api.core.composite.field;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.*;

public interface FieldCompositeService {
    @GetMapping(
            value ="/field-composite/{fieldId}",
            produces = "application/json")
    Mono<String> getCompositeField(@PathVariable int fieldId);

    @GetMapping(
            value ="/field-composite",
            produces = "application/json")
    Flux<String> getCompositeFields();
}
