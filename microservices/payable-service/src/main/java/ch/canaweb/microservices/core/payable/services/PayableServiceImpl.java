package ch.canaweb.microservices.core.payable.services;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Payable.PayableService;
import ch.canaweb.microservices.core.payable.persistence.PayableRepository;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.DupliateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PayableServiceImpl implements PayableService {

    private static final Logger LOG = LoggerFactory.getLogger(PayableServiceImpl.class);

    private final PayableRepository repository;
    private final PayableMapper mapper;

    @Autowired
    public PayableServiceImpl(PayableRepository repository, PayableMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Payable> getPayable(int payableId) {
        LOG.info("Fetch Payable with id: " + payableId);
        return repository.findByPayableId(payableId)
                .log()
                .onErrorMap( Exception.class, ex -> ex)
                .map(mapper::entityToApi)
                .switchIfEmpty(Mono.error(new DataNotFoundException("No Payable found for payableId: " + payableId)));
    }

    @Override
    public Flux<Payable> getAllPayablesForField(int fieldId) {
        LOG.info("Fetch payables for fieldID: " + fieldId);
        return repository.findByFieldId(fieldId)
                .log()
                .onErrorMap( Exception.class, ex -> ex)
                .map(mapper::entityToApi)
                .switchIfEmpty(Flux.error(new DataNotFoundException("No Payable found for payableId: " + fieldId)));
    }

    @Override
    public Mono<Void> deleteAllPayablesForField(int fieldId) {
        LOG.info("Delete Payables with fieldId: " + fieldId);
        return repository.deleteAllByFieldId(fieldId)
                .log();
    }

    @Override
    public Flux<Payable> getAllPayables() {
        LOG.info("Fetch all Payables.");
        return repository.findAll()
                .log()
                .onErrorMap( Exception.class, ex -> ex)
                .map(mapper::entityToApi)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Flux<Payable> getAllPayablesBetween(LocalDate from, LocalDate to) {
        LOG.info("Fetch payables between: " + from.format(DateTimeFormatter.ISO_DATE) + " - " + to.format(DateTimeFormatter.ISO_DATE) + ".");
        return repository.findByTransactionDateBetween(from, to)
                .map(mapper::entityToApi)
                .log();
    }

    @Override
    public Mono<Payable> createPayable(Payable body) {
        LOG.info("Create new Payable with Id: " + body.getPayableId());
        return repository.insert(mapper.apiToEntity(body))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> {
                            LOG.error(ex.getMessage());
                            return new DupliateException("Duplicate key, FieldId: " + body.getFieldId());
                        }
                )
                .map(mapper::entityToApi)
                .log();
    }

    public Flux<Payable> createPayables(List<Payable> payables) {
        LOG.info("Create new Payables: " + payables.size());

        return repository.insert(payables.stream().map(mapper::apiToEntity).collect(Collectors.toList()))
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> {
                            LOG.error(ex.getMessage());
                            return new DupliateException("Duplicate key, FieldId: " + ex.getMessage());
                        }
                )
                .map(mapper::entityToApi)
                .log();
    }

    @Override
    public Mono<Payable> updatePayable(Payable body) {
        LOG.info("Update Payable with ID: " + body.getPayableId());
        return repository.findByPayableId(body.getPayableId())
                .log()
                .flatMap(existingPayable -> {
                    mapper.updateEntityWithApi(existingPayable, body);
                    return repository.save(existingPayable).flatMap(
                            updatedPayable -> Mono.just(mapper.entityToApi(updatedPayable))
                    );
                })
                .switchIfEmpty(Mono.error(new DataNotFoundException("test")));
    }

    @Override
    public Mono<Void> deletePayable(int payableId) {
        LOG.info("Update Payable with ID: " + payableId);
        return repository.deleteByPayableId(payableId)
                .log();
    }
}
