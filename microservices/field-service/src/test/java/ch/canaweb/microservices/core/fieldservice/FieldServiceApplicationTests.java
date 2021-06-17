package ch.canaweb.microservices.core.fieldservice;


import ch.canaweb.api.core.Field.Field;
import ch.canaweb.microservices.core.persistence.FieldEntity;
import ch.canaweb.microservices.core.persistence.FieldRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

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
    }

    @Test
    public void getFieldById() {

        int fieldId = 1;

        assertNull(repository.findByFieldId(fieldId).block());
        assertEquals(0, (long)repository.count().block());

        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();

        assertNotNull(repository.findByFieldId(fieldId).block());
        assertEquals(1, (long)repository.count().block());

        WebTestClient.BodyContentSpec res = getAndVerifyField(fieldId, OK);
        res.jsonPath("$.fieldId").isEqualTo(fieldId);
    }

    @Test
    public void getFieldByIdNonExistant() {
        int fieldId = 1;
        int nonExistantFieldId = 8;

        assertNull(repository.findByFieldId(fieldId).block());
        assertEquals(0, (long)repository.count().block());

        FieldEntity entity = new FieldEntity(fieldId, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity).block();

        assertNotNull(repository.findByFieldId(fieldId).block());
        assertEquals(1, (long)repository.count().block());

        WebTestClient.BodyContentSpec res = getAndVerifyField(nonExistantFieldId, HttpStatus.INTERNAL_SERVER_ERROR);
        System.out.println(res.returnResult());
    }

    @Test
    public void getAllFields() throws JSONException, JsonProcessingException {
        assertEquals(0, (long)repository.count().block());

        FieldEntity entity1 = new FieldEntity(1, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        FieldEntity entity2 = new FieldEntity(2, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        FieldEntity entity3 = new FieldEntity(3, "oinky_field", "oinky_the_owner", 22.0, 88.8, new Date(), "oinky-ingenio-id", new Date());
        repository.insert(entity1).block();
        repository.insert(entity2).block();
        repository.insert(entity3).block();

        assertEquals(3, (long)repository.count().block());

        WebTestClient.BodyContentSpec r = getAndVerifyField("", OK);
        EntityExchangeResult<byte[]> a = r.returnResult();
        String str = new String(a.getResponseBody());
        JSONArray jsonArray = new JSONArray(str);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Field> fields = Arrays.asList(objectMapper.readValue(str, Field[].class));
        assertEquals(3, fields.size());
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


}
