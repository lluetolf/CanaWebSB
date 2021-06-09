package ch.canaweb.microservices.core.services;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FieldServiceImpl implements FieldService {
    @Override
    public Field getField(int fieldId) {
        return null;
    }

    @Override
    public Field getAllFields() {
        return null;
    }
}
