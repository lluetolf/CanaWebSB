package ch.canaweb.api.core.Field;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FieldService {

    /**
     * Sample usage: curl $HOST:$PORT/field/1
     *
     * @param fieldId
     * @return the field, if found, else null
     */
    @GetMapping(
            value    = "/field/{fieldId}",
            produces = "application/json")
    Field getField(@PathVariable int fieldId);

    /**
     * Sample usage: curl $HOST:$PORT/field
     *
     * @return all fields
     */
    @GetMapping(
            value    = "/field",
            produces = "application/json")
    Field getAllFields();
}
