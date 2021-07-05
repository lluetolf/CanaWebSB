package ch.canaweb.microservices.core.field.services;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import ch.canaweb.microservices.core.field.persistence.FieldRepository;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.DupliateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FieldServiceImpl implements FieldService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldServiceImpl.class);

    private final FieldRepository repository;
    private final FieldMapper mapper;

    @Autowired
    public FieldServiceImpl(FieldRepository repository, FieldMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Field> getField(int fieldId) {
        LOG.info("Fetch Field with id: " + fieldId);
        return repository.findByFieldId(fieldId)
                .map(mapper::entityToApi)
                .log()
                .switchIfEmpty(Mono.error(new DataNotFoundException("No field found for fieldId: " + fieldId)));
    }

    @Override
    public Flux<Field> getAllFields() {
        LOG.info("Fetch all fields.");
        return repository.findAll()
                .switchIfEmpty(Flux.empty())
                .map(mapper::entityToApi)
                .log();
    }

    @Override
    public Mono<Field> createField(@RequestBody Field body) {
        LOG.info("Create Field.");
        return repository.insert(mapper.apiToEntity(body))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> {
                            LOG.error(ex.getMessage());
                            return new DupliateException("Duplicate key, FieldId: " + body.getFieldId());
                        }
                )
                .map(mapper::entityToApi);
    }

    @Override
    public Mono<Field> updateField(Field body) {
        LOG.info("Update Field with ID: " + body.getFieldId());
        return repository.findByFieldId(body.getFieldId())
                .log()
                .flatMap(existingField -> {
                    mapper.updateEntityWithApi(existingField, body);
                    return repository.save(existingField).flatMap(
                            updatedField -> Mono.just(mapper.entityToApi(updatedField))
                    );
                })
                .switchIfEmpty(Mono.error(new DataNotFoundException("test")));
    }

    @Override
    public Mono<Void> deleteField(int fieldId) {
        LOG.info("Delete Field with ID: " + fieldId);
        return repository.deleteByFieldId(fieldId)
                .log();
    }
}
