package ch.canaweb.microservices.composite.field.services;

import ch.canaweb.api.composite.field.CompositeField;
import ch.canaweb.api.composite.field.FieldCompositeService;
import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class FieldCompositeServiceImpl implements FieldCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeServiceImpl.class);

    private final FieldCompositeIntegration integration;

    public FieldCompositeServiceImpl(FieldCompositeIntegration integration) {
        this.integration = integration;
    }

    @Override
    public Flux<Field> getAllFields() {
        LOG.info("Get All Fields.");
        return integration.getAllFields();
    }

    @Override
    public Mono<CompositeField> getCompositeField(int fieldId) {
        LOG.info("Get CompositeField with FieldID " + fieldId);
        return Mono.zip(
                values -> new CompositeField((Field) values[0], (List<Payable>) values[1], (List<Receivable>) values[2]),
                integration.getFieldForId(fieldId),
                integration.getPayablesForFieldId(fieldId).collectList(),
                integration.getReceivablesForFieldId(fieldId).collectList())
                .doOnError(ex -> LOG.warn("getCompositeField failed: {}", ex.toString()))
                .switchIfEmpty(Mono.error(new DataNotFoundException("No CompositeField with for id: " + fieldId)))
                .log();
    }

    @Override
    public Flux<CompositeField> getCompositeFields() {
        LOG.info("Get All CompositeFields.");
        Flux<Field> fields = integration.getAllFields();

        return fields.flatMap( f->
                Flux.zip(
                        integration.getPayablesForFieldId(f.getFieldId()).collectList(),
                        integration.getReceivablesForFieldId(f.getFieldId()).collectList(),
                        (p,r) -> new CompositeField(f, p ,r)
                ))
                .doOnError(ex -> LOG.warn("getCompositeField failed: {}", ex.toString()))
                .log();
    }

    @Override
    public Mono<Void> updateCompositeField() {
        return null;
    }

    @Override
    public CompositeField createCompositeField(CompositeField body) {
        if(body == null) {
            LOG.warn("Tried create CompositeField with no content.");
            throw new InvalidInputException("Body empty");
        }
        Field field = body.getField();
        List<Payable> payables = body.getPayables();
        List<Receivable> receivables = body.getReceivables();

//        integration.createField(field);
        return null;
    }

    @Override
    public Mono<MicroServiceStatus> getUpstreamMicroServicesStatus() {
        LOG.info("Get upstream microservices status.");
        return integration.getAllUpstreamStatus();
    }

    @Override
    public Mono<Void> deleteField(int fieldId) {
        return null;
    }

}
