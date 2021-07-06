package ch.canaweb.microservices.core.receivable.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReceivableRepository extends ReactiveMongoRepository<ReceivableEntity, String> {

    Mono<ReceivableEntity> findByReceivableId(int receivableId);

    Flux<ReceivableEntity> findByFieldId(int fieldId);

    Flux<ReceivableEntity> findAll();

    Mono<Void> deleteByFieldId(int receivableId);
}
