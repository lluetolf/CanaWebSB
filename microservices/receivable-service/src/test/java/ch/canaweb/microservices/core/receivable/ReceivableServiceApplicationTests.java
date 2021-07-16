package ch.canaweb.microservices.core.receivable;

import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.microservices.core.receivable.persistence.ReceivableEntity;
import ch.canaweb.microservices.core.receivable.persistence.ReceivableRepository;
import ch.canaweb.microservices.core.receivable.services.ReceivableMapper;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@AutoConfigureWebTestClient
class ReceivableServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ReceivableRepository repository;

    @Autowired
    private ReceivableMapper mapper;


    @BeforeEach
    public void setupDb() {
        repository.deleteAll().block();
        assertCount(0);

        repository.insert(Arrays.asList(
                new ReceivableEntity(1, "2020/2021", LocalDate.now(), "document-1", 1, LocalDate.now()),
                new ReceivableEntity(2, "2020/2021", LocalDate.now(), "document-1", 1, LocalDate.now()),
                new ReceivableEntity(3, "2020/2023", LocalDate.now(), "document-3", 3, LocalDate.now()),
                new ReceivableEntity(4, "2020/2024", LocalDate.now(), "document-4", 44, LocalDate.now()),
                new ReceivableEntity(5, "2020/2025", LocalDate.now(), "document-5", 44, LocalDate.now()),
                new ReceivableEntity(6, "2020/2026", LocalDate.now(), "document-6", 44, LocalDate.now()),
                new ReceivableEntity(7, "2020/2027", LocalDate.now(), "document-7", 7, LocalDate.now()),
                new ReceivableEntity(8, "2020/2028", LocalDate.now(), "document-8", 8, LocalDate.now()),
                new ReceivableEntity(9, "2020/2029", LocalDate.now(), "document-9", 9, LocalDate.now()),
                new ReceivableEntity(10, "2020/20210", LocalDate.now(), "document-10", 10, LocalDate.now()),
                new ReceivableEntity(11, "2020/20211", LocalDate.now(), "document-11", 11, LocalDate.now()),
                new ReceivableEntity(12, "2020/20212", LocalDate.now(), "document-12", 12, LocalDate.now())
        )).blockLast();
        assertCount(12);
    }

    @Test
    public void getReceivableByReceivableId() {
        int receivableId = 3;
        assertNotNull(repository.findByReceivableId(receivableId).block());

        WebTestClient.BodyContentSpec res = getAndVerifyReceivable(receivableId, HttpStatus.OK);
        System.out.println(res);
    }

    @Test
    public void getReceivableByReceivableIdNonExistent() {
        int receivableId = 88;

        WebTestClient.BodyContentSpec res = getAndVerifyReceivable(receivableId, HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAllReceivables() throws JsonProcessingException {
        WebTestClient.BodyContentSpec r = getAndVerifyReceivable("", HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        List<Receivable> receivables = Arrays.asList(objectMapper.readValue(str, Receivable[].class));
        assertEquals(12, receivables.size());
    }

    @Test
    public  void getReceivablesByFieldId() throws JsonProcessingException {
        int fieldId = 44;

        WebTestClient.BodyContentSpec r = getAndVerifyReceivable("/field/" + fieldId, HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        List<Receivable> receivables = Arrays.asList(objectMapper.readValue(str, Receivable[].class));
        assertEquals(3, receivables.size());
    }

    @Test
    public  void getReceivableByFieldIdNonExistent() {
        int fieldId = 88;

        WebTestClient.BodyContentSpec r = getAndVerifyReceivable("/field/" + fieldId, HttpStatus.NOT_FOUND);
    }

    private WebTestClient.BodyContentSpec getAndVerifyReceivable(int receivableId, HttpStatus expectedStatus) {
        return getAndVerifyReceivable("/" + receivableId, expectedStatus);
    }

    @Test
    public void createReceivable() {
        Receivable newReceivable = new Receivable(888, "2888/2888", LocalDate.now(), "new-9", 888, LocalDate.now());

        client.post()
                .uri("/receivable")
                .body(Mono.just(newReceivable), Receivable.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .returnResult(Receivable.class);

        assertCount(13);
    }

    @Test
    public void updateReceivable() {
        int receivableId = 10;

        ReceivableEntity oldReceivable = repository.findByReceivableId(receivableId).block();
        Receivable updatedReceivable = new Receivable(receivableId, "555/555", LocalDate.now().minusDays(555), "updated-555", 888, LocalDate.now().minusDays(555));

        client.patch()
                .uri("/receivable")
                .body(Mono.just(updatedReceivable), Receivable.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Receivable.class);

        ReceivableEntity newReceivable = repository.findByReceivableId(receivableId).block();

        assertEquals(Objects.requireNonNull(oldReceivable.getId()), Objects.requireNonNull(newReceivable.getId()));
        assertEquals(oldReceivable.getReceivableId(), newReceivable.getReceivableId());

        assertNotEquals(oldReceivable.getDocumentId(), newReceivable.getDocumentId());
        assertNotEquals(oldReceivable.getZafra(), newReceivable.getZafra());

        assertNotEquals(oldReceivable.getTransactionDate(), newReceivable.getTransactionDate());
        assertNotEquals(oldReceivable.getLastUpdated(), newReceivable.getLastUpdated());

        assertNotEquals(oldReceivable.getFieldId(), newReceivable.getFieldId());
    }

    @Test
    public void updateReceivableNonExistent() {
        int receivableId = 999;

        Receivable updatedReceivable = new Receivable(receivableId, "555/555", LocalDate.now().minusDays(555), "updated-555", 888, LocalDate.now().minusDays(555));

        client.patch()
                .uri("/receivable")
                .body(Mono.just(updatedReceivable), Receivable.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void deleteReceivable() {
        int existingReceivableId = 10;
        int noneExistingReceivableId = 777;

        client.delete()
                .uri("/receivable/" + existingReceivableId)
                .exchange()
                .expectStatus().isOk();
        getAndVerifyReceivable(existingReceivableId, HttpStatus.NOT_FOUND);

        client.delete()
                .uri("/receivable/" + noneExistingReceivableId)
                .exchange()
                .expectStatus().isOk();
    }

    private WebTestClient.BodyContentSpec getAndVerifyReceivable(String productIdPath, HttpStatus expectedStatus) {
        String uri = "/receivable" + productIdPath;
        System.out.println("URI: " + uri);
        return client.get()
                .uri(uri)
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
