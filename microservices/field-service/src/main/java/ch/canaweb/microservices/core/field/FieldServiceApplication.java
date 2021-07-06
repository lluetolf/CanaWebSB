package ch.canaweb.microservices.core.field;

import ch.canaweb.microservices.core.field.persistence.FieldEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.index.ReactiveIndexOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;



@SpringBootApplication
@ComponentScan("ch.canaweb")
public class FieldServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(FieldServiceApplication.class);


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(FieldServiceApplication.class, args);

        String mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        String mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        String mongodDbDatabase = ctx.getEnvironment().getProperty("spring.data.mongodb.database");
        LOG.info("Connected to MongoDb: " + mongodDbHost + ":" + mongodDbPort + "/" + mongodDbDatabase);

        String canaweb_env = ctx.getEnvironment().getProperty("canaweb_env");
        LOG.info("CanaWeb Env: " + canaweb_env);
    }

    @Autowired
    ReactiveMongoOperations mongoTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {

        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

        ReactiveIndexOperations indexOps = mongoTemplate.indexOps(FieldEntity.class);
        resolver.resolveIndexFor(FieldEntity.class).forEach(e -> indexOps.ensureIndex(e).block());
    }

}
