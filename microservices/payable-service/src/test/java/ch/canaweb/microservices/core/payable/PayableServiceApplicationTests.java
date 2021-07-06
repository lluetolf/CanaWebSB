package ch.canaweb.microservices.core.payable;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.microservices.core.payable.persistence.PayableEntity;
import ch.canaweb.microservices.core.payable.persistence.PayableRepository;
import ch.canaweb.microservices.core.payable.services.PayableMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@AutoConfigureWebTestClient
class PayableServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private PayableRepository repository;

    @Autowired
    private PayableMapper mapper;


    @BeforeEach
    public void setupDb() {
        repository.deleteAll().block();
        assertCount(0);

        repository.insert(Arrays.asList(
                new PayableEntity(1, DateUtils.addDays(new Date(), -1), 1.0, 1.0, 1, 1, "Category-1", "SubCategory-1", "Comment-1", new Date()),
                new PayableEntity(2, DateUtils.addDays(new Date(), -2), 2.0, 2.0, 2, 2, "Category-2", "SubCategory-2", "Comment-2", new Date()),
                new PayableEntity(3, DateUtils.addDays(new Date(), -3), 3.0, 3.0, 3, 3, "Category-3", "SubCategory-3", "Comment-3", new Date()),
                new PayableEntity(4, DateUtils.addDays(new Date(), -4), 4.0, 4.0, 4, 4, "Category-4", "SubCategory-4", "Comment-4", new Date()),
                new PayableEntity(5, DateUtils.addDays(new Date(), -5), 5.0, 5.0, 5, 5, "Category-5", "SubCategory-5", "Comment-5", new Date()),
                new PayableEntity(6, DateUtils.addDays(new Date(), -6), 6.0, 6.0, 6, 6, "Category-6", "SubCategory-6", "Comment-6", new Date()),
                new PayableEntity(7, DateUtils.addDays(new Date(), -7), 7.0, 7.0, 7, 44, "Category-7", "SubCategory-7", "Comment-7", new Date()),
                new PayableEntity(8, DateUtils.addDays(new Date(), -8), 8.0, 8.0, 8, 44, "Category-8", "SubCategory-8", "Comment-8", new Date()),
                new PayableEntity(9, DateUtils.addDays(new Date(), -9), 9.0, 9.0, 9, 44, "Category-9", "SubCategory-9", "Comment-9", new Date()),
                new PayableEntity(10, DateUtils.addDays(new Date(), -10), 10.0, 10.0, 10, 10, "Category-10", "SubCategory-10", "Comment-10", new Date()),
                new PayableEntity(11, DateUtils.addDays(new Date(), -11), 11.0, 11.0, 11, 11, "Category-11", "SubCategory-11", "Comment-11", new Date()),
                new PayableEntity(12, DateUtils.addDays(new Date(), -12), 12.0, 12.0, 12, 12, "Category-12", "SubCategory-12", "Comment-12", new Date())
        )).blockLast();
        assertCount(12);
    }

    @Test
    public void getPayableByPayableId() {
        int payableId = 3;
        assertNotNull(repository.findByPayableId(payableId).block());

        WebTestClient.BodyContentSpec res = getAndVerifyPayable(payableId, HttpStatus.OK);
        System.out.println(res);
    }

    @Test
    public void getPayableByPayableIdNonExistent() {
        int payableId = 88;

        WebTestClient.BodyContentSpec res = getAndVerifyPayable(payableId, HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAllPayables() throws JsonProcessingException {
        WebTestClient.BodyContentSpec r = getAndVerifyPayable("", HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Payable> payables = Arrays.asList(objectMapper.readValue(str, Payable[].class));
        assertEquals(12, payables.size());
    }

    @Test
    public  void getPayablesByFieldId() throws JsonProcessingException {
        int fieldId = 44;

        WebTestClient.BodyContentSpec r = getAndVerifyPayable("/field/" + fieldId, HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        String str = new String(a.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Payable> payables = Arrays.asList(objectMapper.readValue(str, Payable[].class));
        assertEquals(3, payables.size());
    }

    @Test
    public  void getPayableByFieldIdNonExistent() {
        int fieldId = 88;

        WebTestClient.BodyContentSpec r = getAndVerifyPayable("/field/" + fieldId, HttpStatus.NOT_FOUND);
    }

    @Test
    public void createPayable() {
        Payable newPayable = new Payable(77, new Date(), 1.0, 1.0, 1, 1, "Category-1", "SubCategory-1", "Comment-1", new Date());

        client.post()
                .uri("/payable")
                .body(Mono.just(newPayable), Payable.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .returnResult(Payable.class);

        assertCount(13);
    }

    @Test
    public void updatePayable() {
        int payableId = 10;

        PayableEntity oldPayable = repository.findByPayableId(payableId).block();
        Payable updatedPayable = new Payable(payableId, DateUtils.addDays(new Date(), 1000), 1000.0, 1000.0, 1000, 1000, "updated", "updated", "updated", DateUtils.addDays(new Date(), 1000));

        client.put()
                .uri("/payable")
                .body(Mono.just(updatedPayable), Payable.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payable.class);

        PayableEntity newPayable = repository.findByPayableId(payableId).block();

        assertEquals(oldPayable.getId(), newPayable.getId());

        assertNotEquals(oldPayable.getCategory(), newPayable.getCategory());
        assertNotEquals(oldPayable.getSubCategory(), newPayable.getSubCategory());
        assertNotEquals(oldPayable.getComment(), newPayable.getComment());

        assertNotEquals(oldPayable.getTransactionDate(), newPayable.getTransactionDate());
        assertNotEquals(oldPayable.getLastUpdated(), newPayable.getLastUpdated());

        assertNotEquals(oldPayable.getPricePerUnit(), newPayable.getPricePerUnit());
        assertNotEquals(oldPayable.getQuantity(), newPayable.getQuantity());
        assertNotEquals(oldPayable.getDocumentId(), newPayable.getDocumentId());
        assertNotEquals(oldPayable.getFieldId(), newPayable.getFieldId());
    }

    @Test
    public void updatePayableNonExistent() {
        int payableId = 999;

        Payable updatedPayable = new Payable(payableId, DateUtils.addDays(new Date(), 1000), 1000.0, 1000.0, 1000, 1000, "updated", "updated", "updated", DateUtils.addDays(new Date(), 1000));

        client.put()
                .uri("/payable")
                .body(Mono.just(updatedPayable), Payable.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void deletePayable() {
        int existingPayableId = 10;
        int noneExistingPayableId = 777;

        client.delete()
                .uri("/payable/" + existingPayableId)
                .exchange()
                .expectStatus().isOk();
        getAndVerifyPayable(existingPayableId, HttpStatus.NOT_FOUND);

        client.delete()
                .uri("/payable/" + noneExistingPayableId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getAllPayablesBetween() throws JsonProcessingException {
        LocalDate from = LocalDate.now().minusDays(3);
        LocalDate to = LocalDate.now();
        String uri = "/between?from=" + DateTimeFormatter.ISO_LOCAL_DATE.format(from)  + "&to=" +  DateTimeFormatter.ISO_LOCAL_DATE.format(to);
        WebTestClient.BodyContentSpec r = getAndVerifyPayable(uri, HttpStatus.OK);

        EntityExchangeResult<byte[]> a = r.returnResult();
        assertNotNull(a.getResponseBody());

        List<Payable> allPayables = repository.findAll()
                .map(mapper::entityToApi)
                .buffer().blockLast();

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
