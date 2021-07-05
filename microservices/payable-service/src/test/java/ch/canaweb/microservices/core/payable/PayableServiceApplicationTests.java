package ch.canaweb.microservices.core.payable;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.microservices.core.payable.persistence.PayableEntity;
import ch.canaweb.microservices.core.payable.persistence.PayableRepository;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@AutoConfigureWebTestClient
class PayableServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private PayableRepository repository;


    @BeforeEach
    public void setupDb() {
        repository.deleteAll().block();
        assertCount(0);

        PayableEntity entity = new PayableEntity(1, new Date(), 3.0, 3.0, 3, 3, "Category-Oinky", "SubCategory-Oinky", "Comment-Oinky", new Date());
        repository.insert(entity).block();
        entity = new PayableEntity(2, new Date(), 13.0, 13.0, 13, 13, "Category-Poinky", "SubCategory-Poinky", "Comment-Poinky", new Date());
        repository.insert(entity).block();
        entity = new PayableEntity(3, new Date(), 23.0, 23.0, 23, 23, "Category-Grinch", "SubCategory-Grinch", "Comment-Grinch", new Date());
        repository.insert(entity).block();
        assertCount(3);
    }

    @Test
    public void getPayableByPayableId() {
        assertCount(3);
        int payableId = 3;
        assertNotNull(repository.findByPayableId(payableId).block());

        WebTestClient.BodyContentSpec res = getAndVerifyPayable(payableId, HttpStatus.OK);
        System.out.println(res);
    }

    @Test
    public void getAllPayables() throws JsonProcessingException {
        assertCount(3);

        WebTestClient.BodyContentSpec r = getAndVerifyPayable("", HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Payable> payables = Arrays.asList(objectMapper.readValue(str, Payable[].class));
        assertEquals(3, payables.size());
    }

    private WebTestClient.BodyContentSpec getAndVerifyPayable(int payableId, HttpStatus expectedStatus) {
        return getAndVerifyPayable("/" + payableId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyPayable(String productIdPath, HttpStatus expectedStatus) {
        String uri = "/payable" + productIdPath;
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
