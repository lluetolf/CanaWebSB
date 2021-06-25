package ch.canaweb.microservices.core.field.services;

import ch.canaweb.microservices.core.field.persistence.FieldEntity;
import ch.canaweb.microservices.core.field.persistence.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

import java.util.Date;


public class ApplicationRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRunner.class);
    private FieldRepository repository;

    public ApplicationRunner(FieldRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Add fields.");
        Flux<FieldEntity> fields = Flux.fromArray( new FieldEntity[] {
                new FieldEntity(1, "oinky_field", "oinky_the_owner", 22.0, 19.2, new Date(), "oinky-ingenio-id", new Date()),
                new FieldEntity(2, "poinky_field", "poinky_the_owner", 22.0, 19.2, new Date(), "poinky-ingenio-id", new Date()),
                new FieldEntity(3, "piggy_field", "piggy_the_owner", 22.0, 19.2, new Date(), "piggy-ingenio-id", new Date())}
                );
        repository.insert(fields).subscribe();
    }
}

