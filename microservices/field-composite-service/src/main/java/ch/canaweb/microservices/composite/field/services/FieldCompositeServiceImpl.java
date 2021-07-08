package ch.canaweb.microservices.composite.field.services;

import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.api.core.composite.field.FieldCompositeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
public class FieldCompositeServiceImpl implements FieldCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeServiceImpl.class);

    private final FieldCompositeIntegration integration;

    public FieldCompositeServiceImpl(FieldCompositeIntegration integration) {
        this.integration = integration;
    }

    @Override
    public Mono<String> getCompositeField(int fieldId) {

        return Mono.just("abc");
    }

    @Override
    public Flux<String> getCompositeFields() {
        List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumped",
                "over",
                "the",
                "lazy",
                "dog"
        );

        return Flux.fromIterable(words);
    }

    @Override
    public Mono<MicroServiceStatus> getUpstreamMicroServicesStatus() {
            return integration.getAllUpstreamStatus();
    }
}
