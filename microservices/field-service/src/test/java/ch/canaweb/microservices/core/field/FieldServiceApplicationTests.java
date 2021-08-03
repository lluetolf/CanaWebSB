package ch.canaweb.microservices.core.field;


import ch.canaweb.api.core.Field.Field;
import ch.canaweb.microservices.core.field.persistence.FieldEntity;
import ch.canaweb.microservices.core.field.persistence.FieldRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0", "eureka.client.enabled=false", "spring.cloud.config.enabled=false"})
@AutoConfigureWebTestClient
class FieldServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private FieldRepository repository;

    @BeforeEach
    public void setupDb() {
        repository.deleteAll().block();
        assertCount(0);

        repository.insert(Arrays.asList(
                new FieldEntity(1, "oinky-1", "oinky_the_owner-1", 1.0, 1.0, LocalDate.now().minusDays(1), "oinky-ingenio-id-1", LocalDate.now().minusDays(1)),
                new FieldEntity(2, "oinky-2", "oinky_the_owner-2", 2.0, 2.0, LocalDate.now().minusDays(2), "oinky-ingenio-id-2", LocalDate.now().minusDays(2)),
                new FieldEntity(3, "oinky-3", "oinky_the_owner-3", 3.0, 3.0, LocalDate.now().minusDays(3), "oinky-ingenio-id-3", LocalDate.now().minusDays(3)),
                new FieldEntity(4, "oinky-4", "oinky_the_owner-4", 4.0, 4.0, LocalDate.now().minusDays(4), "oinky-ingenio-id-4", LocalDate.now().minusDays(4)),
                new FieldEntity(5, "oinky-5", "oinky_the_owner-5", 5.0, 5.0, LocalDate.now().minusDays(5), "oinky-ingenio-id-5", LocalDate.now().minusDays(5)),
                new FieldEntity(6, "oinky-6", "oinky_the_owner-6", 6.0, 6.0, LocalDate.now().minusDays(6), "oinky-ingenio-id-6", LocalDate.now().minusDays(6)),
                new FieldEntity(7, "oinky-7", "oinky_the_owner-7", 7.0, 7.0, LocalDate.now().minusDays(7), "oinky-ingenio-id-7", LocalDate.now().minusDays(7)),
                new FieldEntity(8, "oinky-8", "oinky_the_owner-8", 8.0, 8.0, LocalDate.now().minusDays(8), "oinky-ingenio-id-8", LocalDate.now().minusDays(8)),
                new FieldEntity(9, "oinky-9", "oinky_the_owner-9", 9.0, 9.0, LocalDate.now().minusDays(9), "oinky-ingenio-id-9", LocalDate.now().minusDays(9)),
                new FieldEntity(10, "oinky-10", "oinky_the_owner-10", 10.0, 10.0, LocalDate.now().minusDays(10), "oinky-ingenio-id-10", LocalDate.now().minusDays(10)),
                new FieldEntity(11, "oinky-11", "oinky_the_owner-11", 11.0, 11.0, LocalDate.now().minusDays(11), "oinky-ingenio-id-11", LocalDate.now().minusDays(11)),
                new FieldEntity(12, "oinky-12", "oinky_the_owner-12", 12.0, 12.0, LocalDate.now().minusDays(12), "oinky-ingenio-id-12", LocalDate.now().minusDays(12))
        )).blockLast();
        assertCount(12);
    }

    @Test
    public void getFieldByFieldId() {
        int fieldId = 3;
        assertNotNull(repository.findByFieldId(fieldId).block());

        WebTestClient.BodyContentSpec res = getAndVerifyField(fieldId, OK);
        res.jsonPath("$.fieldId").isEqualTo(fieldId);
    }

    @Test
    public void getFieldByFieldIdNonExistent() {
        int nonExistentFieldId = 88;

        getAndVerifyField(nonExistentFieldId, HttpStatus.NOT_FOUND);
    }

    @Test
    public void getFieldByName() {
        String fieldName = "oinky-6";

        assertNotNull(repository.findByName(fieldName).block());

        WebTestClient.BodyContentSpec res = getAndVerifyField("/name/" + fieldName, OK);
        res.jsonPath("$.name").isEqualTo(fieldName);
    }

    @Test
    public void getAllFields() throws JsonProcessingException {
        WebTestClient.BodyContentSpec r = getAndVerifyField("", OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        List<Field> fields = Arrays.asList(objectMapper.readValue(str, Field[].class));
        assertEquals(12, fields.size());
    }

    @Test
    public void createField() {
        Field newField = new Field(666, "oinky_field", "oinky_the_owner", 22.0, 88.8, LocalDate.now(), "oinky-ingenio-id", LocalDate.now());

        client.post()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Field.class);

        assertCount(13);
    }

//    @Test
    //TODO: find a fix
    public void createFieldDuplicate() {
        Field newField = new Field(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, LocalDate.now(), "oinky-ingenio-id", LocalDate.now());

        client.post()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void createFieldInvalidPayload() {
        FieldEntity newField = new FieldEntity(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, LocalDate.now(), "oinky-ingenio-id", LocalDate.now());
        client.post()
                .uri("/field")
                .body(Mono.just(newField), FieldEntity.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void updateField() {
        int fieldId = 10;

        FieldEntity oldPayable = repository.findByFieldId(fieldId).block();
        Field updatedField = new Field(fieldId, "updated_oinky_field", "updated_oinky_the_owner", 122.0, 188.8, LocalDate.now().minusDays(777), "updated_oinky-ingenio-id", LocalDate.now().minusDays(777));

        client.patch()
                .uri("/field")
                .body(Mono.just(updatedField), Field.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Field.class);

        FieldEntity fetchedField = repository.findByFieldId(fieldId).block();
        assertNotNull(fetchedField);

        assertEquals(fetchedField.getFieldId(), updatedField.getFieldId());

        assertNotEquals(oldPayable.getName(), fetchedField.getName());
        assertNotEquals(oldPayable.getCultivatedArea(), fetchedField.getCultivatedArea());
        assertNotEquals(oldPayable.getIngenioId(), fetchedField.getIngenioId());
        assertNotEquals(oldPayable.getLastUpdated(), fetchedField.getLastUpdated());
        assertNotEquals(oldPayable.getAcquisitionDate(), fetchedField.getAcquisitionDate());
    }

    @Test
    public void updateFieldNonExistant() {
        int fieldId = 999;

        Field updatedField = new Field(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, LocalDate.now(), "oinky-ingenio-id", LocalDate.now());

        client.patch()
                .uri("/field")
                .body(Mono.just(updatedField), Field.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void deleteField() {
        int existingFieldId = 10;
        int noneExistingFieldId = 777;

        client.delete()
                .uri("/field/" + existingFieldId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
        getAndVerifyField(existingFieldId, HttpStatus.NOT_FOUND);

        client.delete()
                .uri("/field/" + noneExistingFieldId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        assertCount(11);
    }

    private WebTestClient.BodyContentSpec getAndVerifyField(int fieldId, HttpStatus expectedStatus) {
        return getAndVerifyField("/" + fieldId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyField(String productIdPath, HttpStatus expectedStatus) {
        return client.get()
                .uri("/field" + productIdPath)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private void assertCount(int expectedCnt) {
        Long cnt = repository.count().block();
        assertNotNull(cnt);
        assertEquals(expectedCnt, cnt.longValue());
    }

}
