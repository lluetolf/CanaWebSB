package ch.canaweb.microservices.core.payable.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface PayableRepository extends ReactiveMongoRepository<PayableEntity, String> {

    Flux<PayableEntity> findByFieldId(int fieldId);

    Mono<PayableEntity> findByPayableId(int fieldId);

    Flux<PayableEntity> findAll();

    Mono<Void> deleteByPayableId(int fieldId);

    Flux<PayableEntity> findByTransactionDateBetween(Date from, Date to);
}
