package ch.canaweb.microservices.core.payable.services;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Payable.PayableService;
import ch.canaweb.microservices.core.payable.persistence.PayableRepository;
import ch.canaweb.util.exception.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .log()
                .switchIfEmpty(Mono.error(new DataNotFoundException("No Payable found for payableId: " + payableId)));
    }

    @Override
    public Flux<Payable> getAllPayablesForField(int fieldId) {
        return null;
    }

    @Override
    public Flux<Payable> getAllPayables() {
        LOG.info("Fetch all Payables.");
        return repository.findAll()
                .switchIfEmpty(Flux.empty())
                .map(mapper::entityToApi)
                .log();
    }

    @Override
    public Mono<Payable> createPayable(Payable body) {
        return null;
    }

    @Override
    public Mono<Payable> updatePayable(Payable body) {
        return null;
    }

    @Override
    public Mono<Void> deletePayable(int payableId) {
        return null;
    }
}
