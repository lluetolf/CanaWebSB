package ch.canaweb.microservices.core.fieldservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.Date;

import ch.canaweb.microservices.core.persistence.FieldEntity;
import ch.canaweb.microservices.core.persistence.FieldRepository;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private FieldRepository repository;

    private FieldEntity savedEntity;

    @BeforeEach
   	public void setupDb() {
        StepVerifier.create(repository.deleteAll()).verifyComplete();

        FieldEntity entity = new FieldEntity(1, "oinky_field", "oinky_the_owner", 22.0, 19.2, new Date(), "oinky-ingenio-id", new Date());
        StepVerifier.create(repository.save(entity))
            .expectNextMatches(createdEntity -> {
                savedEntity = createdEntity;
                return areFieldEqual(entity, savedEntity);
            })
            .verifyComplete();
    }


    @Test
   	public void create() {
        FieldEntity newEntity = new FieldEntity(1, "monkey_field", "monkey_the_owner", 22.0, 19.2, new Date(), "monkey-ingenio-id", new Date());

        StepVerifier.create(repository.save(newEntity))
            .expectNextMatches(createdEntity -> newEntity.getFieldId() == createdEntity.getFieldId())
            .verifyComplete();

        StepVerifier.create(repository.findById(newEntity.getId()))
            .expectNextMatches(foundEntity -> areFieldEqual(newEntity, foundEntity))
            .verifyComplete();

        StepVerifier.create(repository.count()).expectNext(2l).verifyComplete();
    }


    @Test
   	public void update() {
        savedEntity.setName("n2");
        StepVerifier.create(repository.save(savedEntity))
            .expectNextMatches(updatedEntity -> updatedEntity.getName().equals("n2"))
            .verifyComplete();

        StepVerifier.create(repository.findById(savedEntity.getId()))
            .expectNextMatches(foundEntity ->
                foundEntity.getVersion() == 1 &&
                foundEntity.getName().equals("n2"))
            .verifyComplete();
    }

    @Test
   	public void delete() {
        StepVerifier.create(repository.delete(savedEntity)).verifyComplete();
        StepVerifier.create(repository.existsById(savedEntity.getId())).expectNext(false).verifyComplete();
    }

    @Test
   	public void getByProductId() {

        StepVerifier.create(repository.findByFieldId(savedEntity.getFieldId()))
            .expectNextMatches(foundEntity -> areFieldEqual(savedEntity, foundEntity))
            .verifyComplete();
    }


    private boolean areFieldEqual(FieldEntity expectedEntity, FieldEntity actualEntity) {
        return
            (expectedEntity.getFieldId() == actualEntity.getFieldId()) &&
            (expectedEntity.getName().equals(actualEntity.getName())) &&
            (expectedEntity.getOwner().equals(actualEntity.getOwner())) &&
            (expectedEntity.getIngenioId().equals(actualEntity.getIngenioId())) &&
            (expectedEntity.getAcquisitionDate().equals(actualEntity.getAcquisitionDate())) &&
            (expectedEntity.getLastUpdated().equals(actualEntity.getLastUpdated())) &&
            (expectedEntity.getCultivatedArea() == actualEntity.getCultivatedArea()) &&
            (expectedEntity.getSize() == actualEntity.getSize());
    }
}
