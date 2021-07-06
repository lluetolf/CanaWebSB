package ch.canaweb.microservices.core.receivable;

import ch.canaweb.microservices.core.receivable.persistence.ReceivableEntity;
import ch.canaweb.microservices.core.receivable.persistence.ReceivableRepository;
import ch.canaweb.microservices.core.receivable.services.ReceivableMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                new ReceivableEntity(4, "2020/2024", LocalDate.now(), "document-4", 4, LocalDate.now()),
                new ReceivableEntity(5, "2020/2025", LocalDate.now(), "document-5", 5, LocalDate.now()),
                new ReceivableEntity(6, "2020/2026", LocalDate.now(), "document-6", 6, LocalDate.now()),
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

    private WebTestClient.BodyContentSpec getAndVerifyReceivable(int payableId, HttpStatus expectedStatus) {
        return getAndVerifyReceivable("/" + payableId, expectedStatus);
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
