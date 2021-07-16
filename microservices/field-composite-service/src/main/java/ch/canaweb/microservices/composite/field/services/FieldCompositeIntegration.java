package ch.canaweb.microservices.composite.field.services;

import ch.canaweb.api.composite.field.CompositeField;
import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.util.exception.DataNotFoundException;
import ch.canaweb.util.exception.InvalidInputException;
import ch.canaweb.util.http.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class FieldCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(FieldCompositeIntegration.class);

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String fieldServiceUrl;
    private final String payableServiceUrl;
    private final String receivableServiceUrl;
    private final String fieldServiceBaseUrl;
    private final String payableServiceBaseUrl;
    private final String receivableServiceBaseUrl;

    public FieldCompositeIntegration(
            WebClient.Builder webClient,
            ObjectMapper mapper,

            @Value("${app.field-service.host}") String fieldServiceHost,
            @Value("${app.field-service.port}") int fieldServicePort,

            @Value("${app.payable-service.host}") String payableServiceHost,
            @Value("${app.payable-service.port}") int payableServicePort,

            @Value("${app.receivable-service.host}") String receivableServiceHost,
            @Value("${app.receivable-service.port}") int receivableServicePort
    ) {
        this.webClient = webClient
                .filter(logRequest())
                .build();

        // Configure Mapper to support LocalDate
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        this.fieldServiceBaseUrl = "http://" + fieldServiceHost + ":" + fieldServicePort;
        this.fieldServiceUrl = this.fieldServiceBaseUrl + "/field/";
        this.payableServiceBaseUrl = "http://" + payableServiceHost + ":" + payableServicePort;
        this.payableServiceUrl = this.payableServiceBaseUrl + "/payable/";
        this.receivableServiceBaseUrl = "http://" + receivableServiceHost + ":" + receivableServicePort;
        this.receivableServiceUrl = this.receivableServiceBaseUrl + "/receivable/";
    }

    public List<Field> getAllFields() {
        return webClient.get().uri(fieldServiceUrl)
                .retrieve()
                .bodyToFlux(Field.class)
                .log()
                .collectList()
                .block();
    }

    public List<Payable> getAllPayables() {
        return webClient.get().uri(payableServiceUrl)
                .retrieve()
                .bodyToFlux(Payable.class)
                .log()
                .collectList()
                .block();
    }

    public List<Receivable> getAllReceivables() {
        return webClient.get().uri(receivableServiceUrl)
                .retrieve()
                .bodyToFlux(Receivable.class)
                .log()
                .collectList()
                .block();
    }


    public List<Payable> getPayablesForFieldId(int fieldId) {
        LOG.info("Call Payables MS:");
        return webClient.get().uri(payableServiceUrl + "field/" + fieldId)
                .retrieve()
                .bodyToFlux(Payable.class)
                .switchIfEmpty(Flux.empty())
                .log()
                .collectList().block();
    }

    public List<Receivable> getReceivablesForFieldId(int fieldId) {
        LOG.info("Call Receivable MS:");
        return webClient.get().uri(receivableServiceUrl + "field/" + fieldId)
                .retrieve()
                .bodyToFlux(Receivable.class)
                .switchIfEmpty(Flux.empty())
                .log()
                .collectList()
                .block();
    }

    public Field getFieldForId(int fieldId) {
        LOG.info("Call Field MS:");
        return webClient.get().uri(fieldServiceUrl  + fieldId)
                .retrieve()
                .bodyToMono(Field.class)
                .switchIfEmpty(Mono.empty())
                .onErrorMap( ex -> handleException(ex))
                .log()
                .block();
    }

    public Field createField(Field field) {
        LOG.info("Create Field with id: " + field);
        return webClient.post().uri(fieldServiceUrl)
                .body(Mono.just(field), Field.class)
                .retrieve()
                .bodyToMono(Field.class)
                .switchIfEmpty(Mono.empty())
                .onErrorMap( ex -> handleException(ex))
                .log()
                .block();
    }

    public List<Payable> createPayables(List<Payable> payables) {
        LOG.info("Create payables number: " + payables.size());

        List<Payable> returnedPayables = new ArrayList<Payable>();
        for (Payable p : payables) {
            returnedPayables.add(
                    webClient.post().uri(payableServiceUrl)
                    .body(Mono.just(p), Payable.class)
                    .retrieve()
                    .bodyToMono(Payable.class)
                    .switchIfEmpty(Mono.empty())
                    .onErrorMap( ex -> handleException(ex))
                    .log()
                    .block());
        }
        return returnedPayables;
    }

    public List<Receivable> createReceivables(List<Receivable> receivables) {
        LOG.info("Create receivables number: " + receivables.size());

        List<Receivable> returnedReceivables = new ArrayList<Receivable>();
        for (Receivable p : receivables) {
            returnedReceivables.add(
                    webClient.post().uri(receivableServiceUrl)
                            .body(Mono.just(p), Receivable.class)
                            .retrieve()
                            .bodyToMono(Receivable.class)
                            .switchIfEmpty(Mono.empty())
                            .onErrorMap( ex -> handleException(ex))
                            .log()
                            .block());
        }
        return returnedReceivables;
    }

    public MicroServiceStatus getAllUpstreamStatus() {
        return Mono.zip(
                values -> new MicroServiceStatus((String) values[0], (String) values[1], (String) values[2]),
                checkServiceHeartBeat(fieldServiceBaseUrl),
                checkServiceHeartBeat(payableServiceBaseUrl ),
                checkServiceHeartBeat(receivableServiceBaseUrl))
                .map(x -> {
                    LOG.info(x.toString());
                    return x;
                })
                .doOnError(ex -> LOG.warn("getAllUpstreamStatus failed: {}", ex.toString()))
                .log()
                .block();
    }

    private Mono<String> checkServiceHeartBeat(String serviceUrl) {
        String uri = "/actuator/health/ping";
        String url = serviceUrl + uri;

        return webClient.get()
                .uri(url)
                .retrieve().bodyToMono(String.class)
                .log()
                .flatMap(s -> {
                    LOG.info("Got: " + s);
                    if (s.contains("UP"))
                        return Mono.just("UP");
                    else
                        return Mono.just("DOWN");
                })
                .onErrorReturn(Exception.class, "DOWN");
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

        WebClientResponseException wcre = (WebClientResponseException) ex;

        switch (wcre.getStatusCode()) {

            case NOT_FOUND:
                return new DataNotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
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

    public void deleteFieldById(int fieldId) {
        webClient.delete().uri(fieldServiceUrl + fieldId)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(ex -> handleException(ex))
                .block();

        webClient.delete().uri(payableServiceUrl + "field/" + fieldId )
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(ex -> handleException(ex))
                .block();

        webClient.delete().uri(receivableServiceUrl + "field/" + fieldId )
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(ex -> handleException(ex))
                .block();
    }

    public CompositeField updateCompositeField(CompositeField cp) {
        int fieldId = cp.getField().getFieldId();
        Field field = getFieldForId(fieldId);
        List<Payable> payables = getPayablesForFieldId(fieldId);
        List<Receivable> receivables = getReceivablesForFieldId(fieldId);

        CompositeField returnValue = new CompositeField(null, new ArrayList<Payable>(), new ArrayList<Receivable>());

        if (!field.equals(cp.getField())) {
            returnValue.setField(
                    webClient.patch()
                            .uri(fieldServiceUrl)
                            .body(Mono.just(cp.getField()), Field.class)
                            .retrieve()
                            .bodyToMono(Field.class)
                            .log()
                            .onErrorMap(ex -> handleException(ex))
                            .block()
            );
        }

        for (Payable updatedPayable : cp.getPayables()) {
            Optional<Payable> presentValue = payables.stream().filter(p -> updatedPayable.getPayableId() == p.getPayableId()).findFirst();
            if(! presentValue.isPresent()) {
                LOG.info("Create Payable with payableId: " + updatedPayable.getPayableId());
                // create new payable
                returnValue.getPayables().add(
                        webClient.post()
                                .uri(payableServiceUrl)
                                .body(Mono.just(updatedPayable), Payable.class)
                                .retrieve()
                                .bodyToMono(Payable.class)
                                .log()
                                .onErrorMap(ex -> handleException(ex))
                                .block()
                );
            } else {
                // remove item from list
                payables.remove(presentValue.get());

                // Check if values have changed and update if needed.
                if(! presentValue.equals(updatedPayable)) {
                    LOG.info("Update Payable with payableId: " + updatedPayable.getPayableId());
                    returnValue.getPayables().add(
                            webClient.patch()
                                    .uri(payableServiceUrl)
                                    .body(Mono.just(updatedPayable), Payable.class)
                                    .retrieve()
                                    .bodyToMono(Payable.class)
                                    .log()
                                    .onErrorMap(ex -> handleException(ex))
                                    .block()
                    );
                } else {
                    returnValue.getPayables().add(presentValue.get());
                }
            }
        }

        // Delete payables only in PV but not in FV.
        for (Payable payableToDelete : payables) {
            LOG.info("Delete Payable with payableId: " + payableToDelete.getPayableId());
            webClient.delete()
                    .uri(payableServiceUrl + payableToDelete.getPayableId())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorMap(ex -> handleException(ex))
                    .block();
        }


        for (Receivable updatedReceivable : cp.getReceivables()) {
            Optional<Receivable> presentValue = receivables.stream().filter(p -> updatedReceivable.getReceivableId() == p.getReceivableId()).findFirst();
            if(! presentValue.isPresent()) {
                LOG.info("Create Receivable with receivableId: " + updatedReceivable.getReceivableId());
                // create new receivable
                returnValue.getReceivables().add(
                        webClient.post()
                                .uri(receivableServiceUrl)
                                .body(Mono.just(updatedReceivable), Receivable.class)
                                .retrieve()
                                .bodyToMono(Receivable.class)
                                .log()
                                .onErrorMap(ex -> handleException(ex))
                                .block()
                );
            } else {
                // remove item from list
                receivables.remove(presentValue.get());

                // Check if values have changed and update if needed.
                if(! presentValue.equals(updatedReceivable)) {
                    LOG.info("Update Receivable with receivableId: " + updatedReceivable.getReceivableId());
                    returnValue.getReceivables().add(
                            webClient.patch()
                                    .uri(receivableServiceUrl)
                                    .body(Mono.just(updatedReceivable), Receivable.class)
                                    .retrieve()
                                    .bodyToMono(Receivable.class)
                                    .log()
                                    .onErrorMap(ex -> handleException(ex))
                                    .block()
                    );
                } else {
                    returnValue.getReceivables().add(presentValue.get());
                }
            }
        }

        // Delete receivables only in PV but not in FV.
        for (Receivable receivableToDelete : receivables) {
            LOG.info("Delete Receivable with receivableId: " + receivableToDelete.getReceivableId());
            webClient.delete()
                    .uri(receivableServiceUrl + receivableToDelete.getReceivableId())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorMap(ex -> handleException(ex))
                    .block();
        }
        
        return  returnValue;
    }
}
