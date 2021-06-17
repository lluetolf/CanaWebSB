package ch.canaweb.microservices.core.persistence;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FieldRepository extends ReactiveMongoRepository<FieldEntity, String> {

    Mono<FieldEntity> findByFieldId(int fieldId);

    Mono<FieldEntity> findByName(String name);

    Flux<FieldEntity> findAll();
}
