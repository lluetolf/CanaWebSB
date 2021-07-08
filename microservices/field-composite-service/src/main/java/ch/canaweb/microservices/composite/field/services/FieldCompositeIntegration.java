package ch.canaweb.microservices.composite.field.services;

import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.InvalidInputException;
import ch.canaweb.util.http.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;


@Component
public class FieldCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeIntegration.class);

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String fieldServiceUrl;
    private final String payableServiceUrl;
    private final String receivableServiceUrl;

    public FieldCompositeIntegration(
            WebClient.Builder webClient,
            ObjectMapper mapper,

            @Value("${app.field-service.host}") String fieldServiceHost,
            @Value("${app.field-service.port}") int    fieldServicePort,

            @Value("${app.payable-service.host}") String payableServiceHost,
            @Value("${app.payable-service.port}") int    payableServicePort,

            @Value("${app.receivable-service.host}") String receivableServiceHost,
            @Value("${app.receivable-service.port}") int    receivableServicePort
    ) {
        this.webClient = webClient
                .filter(logRequest())
                .build();

        // Configure Mapper to support LocalDate
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        this.fieldServiceUrl = "http://" + fieldServiceHost + ":" + fieldServicePort;
        this.payableServiceUrl = "http://" + payableServiceHost + ":" + payableServicePort;
        this.receivableServiceUrl = "http://" + receivableServiceHost + ":" + receivableServicePort;

        checkUpStreamServices();
    }

    public void checkUpStreamServices() {
        LOG.info("Upstream Services:");
        checkServiceHeartBeat(fieldServiceUrl);
        checkServiceHeartBeat(payableServiceUrl);
        checkServiceHeartBeat(receivableServiceUrl);
    }

    public Mono<String> checkServiceHeartBeat(String serviceUrl) {
        String uri = "/actuator/health/ping";
        String url = serviceUrl + uri;
        LOG.info("\tHit: " + url);

        return webClient.get()
                .uri(url)
                .retrieve().bodyToMono(String.class)
                .log()
                .flatMap( s -> {
                    LOG.info("Got: " + s);
                    if(s.contains("UP"))
                        return Mono.just("UP");
                    else
                        return Mono.just("UP");
                })
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    public Mono<MicroServiceStatus> getAllUpstreamStatus() {
        return Mono.zip(
                values -> new MicroServiceStatus((String) values[0], (String) values[1], (String) values[2]),
                checkServiceHeartBeat(fieldServiceUrl),
                checkServiceHeartBeat(payableServiceUrl),
                checkServiceHeartBeat(receivableServiceUrl))
                .map(x -> { LOG.info(x.toString()); return x; })
                .doOnError(ex -> LOG.warn("getAllUpstreamStatus failed: {}", ex.toString()))
                .log();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOG.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOG.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException)ex;

        switch (wcre.getStatusCode()) {

            case NOT_FOUND:
                return new DataNotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY :
                return new InvalidInputException(getErrorMessage(wcre));

            default:
                LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
