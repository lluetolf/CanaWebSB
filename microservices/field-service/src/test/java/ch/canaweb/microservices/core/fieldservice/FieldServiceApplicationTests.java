package ch.canaweb.microservices.core.fieldservice;


import ch.canaweb.api.core.Field.Field;
import ch.canaweb.microservices.core.persistence.FieldEntity;
import ch.canaweb.microservices.core.persistence.FieldRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
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
    }

    @Test
    public void getFieldById() {
        int fieldId = 1;
        assertNull(repository.findByFieldId(fieldId).block());

        addBaseField(fieldId);

        WebTestClient.BodyContentSpec res = getAndVerifyField(fieldId, OK);
        res.jsonPath("$.fieldId").isEqualTo(fieldId);
    }

    @Test
    public void getFieldByIdNonExistent() {
        int nonExistentFieldId = 8;

        WebTestClient.BodyContentSpec res = getAndVerifyField(nonExistentFieldId, HttpStatus.NOT_FOUND);
        System.out.println(res.returnResult());
    }

    @Test
    public void getAllFields() throws JsonProcessingException {

        addBaseField(1);
        addBaseField(2);
        addBaseField(3);
        assertCount(3);

        WebTestClient.BodyContentSpec r = getAndVerifyField("", OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Field> fields = Arrays.asList(objectMapper.readValue(str, Field[].class));
        assertEquals(3, fields.size());
    }

    @Test
    public void createField() {

        Field newField = new Field(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        client.post()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Field.class);

        assertCount(1);
    }

    @Test
    public void duplicateFieldId() {

        Field newField = new Field(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        client.post()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Field.class);

        assertCount(1);

        client.post()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void createFieldInvalidPayload() {
        FieldEntity newField = new FieldEntity(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        client.post()
                .uri("/field")
                .body(Mono.just(newField), FieldEntity.class)
                .exchange()
                .expectStatus().isBadRequest();

        assertCount(0);
    }

    @Test
    public void updateField() {
        int fieldId = 1;

        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();

        Field newField = new Field(fieldId, "updated_oinky_field", "updated_oinky_the_owner", 122.0, 188.8, new Date(), "updated_oinky-ingenio-id", new Date());

        client.put()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Field.class);

        FieldEntity fetchedField = repository.findByFieldId(fieldId).block();

        assertNotNull(fetchedField);
        assertEquals(fetchedField.getFieldId(), newField.getFieldId());
        assertEquals(fetchedField.getName(), newField.getName());
        assertEquals(fetchedField.getCultivatedArea(), newField.getCultivatedArea());
        assertEquals(fetchedField.getIngenioId(), newField.getIngenioId());
        assertEquals(fetchedField.getLastUpdated(), newField.getLastUpdated());
        assertEquals(fetchedField.getAcquisitionDate(), newField.getAcquisitionDate());
    }

    @Test
    public void updateNonExistantField() {
        int fieldId = 1;
        int nonExistentFieldId = 8;

        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();
        assertCount(1);

        Field newField = new Field(nonExistentFieldId,
                "updated_oinky_field",
                "updated_oinky_the_owner",
                122.0, 188.8, new Date(),
                "updated_oinky-ingenio-id",
                new Date());


        client.put()
                .uri("/field")
                .body(Mono.just(newField), Field.class)
                .exchange()
                .expectStatus().isNotFound();

        FieldEntity fetchedField = repository.findByFieldId(fieldId).block();

        assertNotNull(fetchedField);
        assertNotEquals(fetchedField.getFieldId(), newField.getFieldId());
        assertNotEquals(fetchedField.getName(), newField.getName());
        assertNotEquals(fetchedField.getCultivatedArea(), newField.getCultivatedArea());
        assertNotEquals(fetchedField.getIngenioId(), newField.getIngenioId());
        assertNotEquals(fetchedField.getLastUpdated(), newField.getLastUpdated());
        assertNotEquals(fetchedField.getAcquisitionDate(), newField.getAcquisitionDate());
    }

    @Test
    public void deleteField() {
        int fieldId = 1;

        assertCount(0);
        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();
        assertCount(1);

        client.delete()
                .uri("/field/" + fieldId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        assertCount(0);
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

    private void addBaseField(int fieldId) {
        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();
    }
}
