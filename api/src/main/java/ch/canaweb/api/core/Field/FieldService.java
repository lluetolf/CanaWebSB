package ch.canaweb.api.core.Field;

import org.springframework.web.bind.annotation.*;

public interface FieldService {

    @GetMapping(
            value    = "/field/{fieldId}",
            produces = "application/json")
    Field getField(@PathVariable int fieldId);

    @GetMapping(
            value    = "/field",
            produces = "application/json")
    Field getAllFields();

}
