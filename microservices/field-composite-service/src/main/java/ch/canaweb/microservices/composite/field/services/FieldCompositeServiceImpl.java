package ch.canaweb.microservices.composite.field.services;

import ch.canaweb.api.composite.field.CompositeField;
import ch.canaweb.api.composite.field.FieldCompositeService;
import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.util.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FieldCompositeServiceImpl implements FieldCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeServiceImpl.class);

    private final FieldCompositeIntegration integration;

    public FieldCompositeServiceImpl(FieldCompositeIntegration integration) {
        this.integration = integration;
    }

    @Override
    public List<Field> getAllFields() {
        LOG.info("Get All Fields.");
        return integration.getAllFields();
    }

    @Override
    public List<Payable> getAllPayables() {
        LOG.info("Get All Payables.");
        return integration.getAllPayables();
    }

    @Override
    public List<Receivable> getAllReceivables() {
        LOG.info("Get All Receivable.");
        return integration.getAllReceivables();
    }

    @Override
    public CompositeField getCompositeFieldByFieldId(int fieldId) {
        LOG.info("Get CompositeField with FieldID " + fieldId);

        Field field = integration.getFieldForId(fieldId);
        List<Payable> payables = integration.getPayablesForFieldId(fieldId);
        List<Receivable> receivables = integration.getReceivablesForFieldId(fieldId);

        return new CompositeField(field, payables, receivables);
    }

    @Override
    public List<CompositeField> getCompositeFields() {
        LOG.info("Get All CompositeFields.");
        List<Field> fields = integration.getAllFields();

        List<CompositeField> compositeFields = new ArrayList<>();
        fields.forEach( f ->
                compositeFields.add(
                        new CompositeField(
                             f,
                             integration.getPayablesForFieldId(f.getFieldId()),
                             integration.getReceivablesForFieldId(f.getFieldId())
                        )
                )
        );
        return compositeFields;
    }

    @Override
    public CompositeField updateCompositeField() {
        return null;
    }

    @Override
    public CompositeField createCompositeField(CompositeField body) {
        LOG.info("Create new CompositeField with id: " + body.getField());
        if(body == null) {
            LOG.warn("Tried create CompositeField with no content.");
            throw new InvalidInputException("Body empty");
        }

        Field field = body.getField();
        List<Payable> payables = body.getPayables();
        List<Receivable> receivables = body.getReceivables();

        if(! payables.stream().filter(p -> p.getFieldId() != field.getFieldId()).collect(Collectors.toList()).isEmpty() ||
               ! receivables.stream().filter(p -> p.getFieldId() != field.getFieldId()).collect(Collectors.toList()).isEmpty() ) {
            throw new InvalidInputException("Invalid payable or receivables.");
        }
        // Try to create field
        Field createdField = integration.createField(field);
        List<Payable> createdPayables = integration.createPayables(payables);
        List<Receivable> createdReceivables = integration.createReceivables(receivables);

        return new CompositeField(createdField, createdPayables, createdReceivables);
    }


    @Override
    public void deleteField(int fieldId) {
        LOG.info("Delete composite-field with fieldId: " + fieldId);
        integration.deleteFieldById(fieldId);
    }

    @Override
    public MicroServiceStatus getUpstreamMicroServicesStatus() {
        LOG.info("Get upstream microservices status.");
        return integration.getAllUpstreamStatus();
    }


}
