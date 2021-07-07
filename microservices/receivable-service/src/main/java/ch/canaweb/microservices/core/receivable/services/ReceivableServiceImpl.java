package ch.canaweb.microservices.core.receivable.services;

import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.api.core.Receivable.ReceivableService;
import ch.canaweb.microservices.core.receivable.persistence.ReceivableRepository;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.DupliateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReceivableServiceImpl implements ReceivableService {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivableServiceImpl.class);

    private final ReceivableRepository repository;
    private final ReceivableMapper mapper;

    @Autowired
    public ReceivableServiceImpl(ReceivableRepository repository, ReceivableMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Receivable> getReceivable(int receivableId) {
        LOG.info("Fetch Receivable with id: " + receivableId);
        return repository.findByReceivableId(receivableId)
                .log()
                .onErrorMap(Exception.class, ex -> ex)
                .map(mapper::entityToApi)
                .switchIfEmpty(Mono.error(new DataNotFoundException("No Receivable found for receivableId: " + receivableId)));
    }

    @Override
    public Flux<Receivable> getReceivableForField(int fieldId) {
        LOG.info("Fetch Receivables with fieldId: " + fieldId);
        return repository.findByFieldId(fieldId)
                .log()
                .onErrorMap(Exception.class, ex -> ex)
                .map(mapper::entityToApi)
                .switchIfEmpty(Flux.error(new DataNotFoundException("No Receivable found for fieldId: " + fieldId)));
    }

    @Override
    public Flux<Receivable> getAllReceivables() {
        LOG.info("Fetch all Receivables");
        return repository.findAll()
            .log()
            .onErrorMap( Exception.class, ex -> ex)
            .map(mapper::entityToApi)
            .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Receivable> createReceivable(Receivable body) {
        LOG.info("Create new Receivable with Id: " + body.getReceivableId());
        return repository.insert(mapper.apiToEntity(body))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> {
                            LOG.error(ex.getMessage());
                            return new DupliateException("Duplicate key, ReceivableId: " + body.getReceivableId());
                        }
                )
                .map(mapper::entityToApi)
                .log();
    }

    @Override
    public Mono<Receivable> updateReceivable(Receivable body) {
        LOG.info("Update Receivable with ID: " + body.getReceivableId());
        return repository.findByReceivableId(body.getReceivableId())
                .log()
                .flatMap(existingReceivable -> {
                    mapper.updateEntityWithApi(existingReceivable, body);
                    return repository.save(existingReceivable).flatMap(
                            updatedReceivable -> Mono.just(mapper.entityToApi(updatedReceivable))
                    );
                })
                .switchIfEmpty(Mono.error(new DataNotFoundException("test")));
    }

    @Override
    public Mono<Void> deleteReceivable(int receivableId) {
        LOG.info("Update Receivable with ID: " + receivableId);
        return repository.deleteByReceivableId(receivableId)
                .log();
    }
}
