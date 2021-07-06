package ch.canaweb.microservices.core.receivable.services;

import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.api.core.Receivable.ReceivableService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReceivableServiceImpl implements ReceivableService {
    @Override
    public Mono<Receivable> getReceivable(int receivableId) {
        return null;
    }

    @Override
    public Flux<Receivable> getAllReceivables() {
        return null;
    }
}
