package ch.canaweb.microservices.core.services;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Field.FieldService;
import ch.canaweb.microservices.core.persistence.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static reactor.core.publisher.Mono.error;

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
                .switchIfEmpty(error(new Exception("No field found for fieldId: " + fieldId)))
                .map( e -> { LOG.info("Entity FieldId: " + e.getFieldId()); return mapper.entityToApi(e); })
                .map( e -> { LOG.info("API FieldId: " + e.getFieldId()); return e; } )
                .doOnError( ex -> ex.toString())
                .log();
    }

    @Override
    public Flux<Field> getAllFields() {
        LOG.info("Fetch all fields.");
        return repository.findAll()
                .switchIfEmpty(Flux.empty())
                .map(e -> mapper.entityToApi(e))
                .doOnError( ex -> ex.toString())
                .log();
    }

    @Override
    public Mono<Field> createField(Field body) {
        return null;
    }

    @Override
    public Mono<Field> updateField(Field body) {
        return null;
    }

    @Override
    public void deleteField(int fieldId) {

    }
}
