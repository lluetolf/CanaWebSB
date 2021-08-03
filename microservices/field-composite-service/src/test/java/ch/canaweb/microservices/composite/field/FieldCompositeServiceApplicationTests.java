package ch.canaweb.microservices.composite.field;

import ch.canaweb.api.composite.field.CompositeField;
import ch.canaweb.api.composite.field.MicroServiceStatus;
import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.microservices.composite.field.services.FieldCompositeIntegration;
import ch.canaweb.util.exception.DataNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0", "eureka.client.enabled=false", "spring.cloud.config.enabled=false"})
@AutoConfigureWebTestClient
class FieldCompositeServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @MockBean
    private FieldCompositeIntegration compositeIntegration;

    @BeforeEach
    public void setUp() {

        when(compositeIntegration.getAllFields()).
                thenReturn(
                        List.of(
                                new Field(1, "oinky-1", "oinky_the_owner-1", 1.0, 1.0, LocalDate.now().minusDays(1), "oinky-ingenio-id-1", LocalDate.now().minusDays(1)),
                                new Field(2, "oinky-2", "oinky_the_owner-2", 2.0, 2.0, LocalDate.now().minusDays(2), "oinky-ingenio-id-2", LocalDate.now().minusDays(2)),
                                new Field(3, "oinky-3", "oinky_the_owner-3", 3.0, 3.0, LocalDate.now().minusDays(3), "oinky-ingenio-id-3", LocalDate.now().minusDays(3))
                        )
                );
        when(compositeIntegration.getFieldForId(1)).
                thenReturn(
                        new Field(1, "oinky-1", "oinky_the_owner-1", 1.0, 1.0, LocalDate.now().minusDays(1), "oinky-ingenio-id-1", LocalDate.now().minusDays(1))
                );

        when(compositeIntegration.createField(any(Field.class))).
                thenReturn(
                        new Field(1, "oinky-1", "oinky_the_owner-1", 1.0, 1.0, LocalDate.now().minusDays(1), "oinky-ingenio-id-1", LocalDate.now().minusDays(1))
                );


        when(compositeIntegration.getFieldForId(88)).
                thenThrow(new DataNotFoundException("No Field found with id 88"));

        when(compositeIntegration.getPayablesForFieldId(1)).
                thenReturn(
                        List.of(
                                new Payable(1, LocalDate.now().minusDays(1), 1.0, 1.0, 1, 1, "Category-1", "SubCategory-1", "Comment-1", LocalDate.now()),
                                new Payable(2, LocalDate.now().minusDays(2), 2.0, 2.0, 2, 1, "Category-2", "SubCategory-2", "Comment-2", LocalDate.now())
                        )
                );
        when(compositeIntegration.getPayablesForFieldId(2)).
                thenReturn(
                        Collections.<Payable>emptyList()
                );
        when(compositeIntegration.getPayablesForFieldId(3)).
                thenReturn(
                        List.of(
                                new Payable(8, LocalDate.now().minusDays(8), 8.0, 8.0, 8, 3, "Category-8", "SubCategory-8", "Comment-8", LocalDate.now())
                        )
                );
        when(compositeIntegration.getPayablesForFieldId(88)).
                thenReturn(
                        Collections.<Payable>emptyList()
                );

        when(compositeIntegration.getReceivablesForFieldId(1)).
                thenReturn(
                        List.of(
                                new Receivable(1, "2020/2021", LocalDate.now(), "document-1", 1, LocalDate.now()),
                                new Receivable(2, "2020/2021", LocalDate.now(), "document-2", 1, LocalDate.now()),
                                new Receivable(3, "2020/2021", LocalDate.now(), "document-3", 1, LocalDate.now())
                        )
                );

        when(compositeIntegration.createReceivables(any(List.class))).
                thenReturn(
                        List.of(
                                new Receivable(1, "2020/2021", LocalDate.now(), "document-1", 1, LocalDate.now()),
                                new Receivable(2, "2020/2021", LocalDate.now(), "document-2", 1, LocalDate.now()),
                                new Receivable(3, "2020/2021", LocalDate.now(), "document-3", 1, LocalDate.now())
                        )
                );
        when(compositeIntegration.getReceivablesForFieldId(2)).
                thenReturn(
                        Collections.<Receivable>emptyList()
                );
        when(compositeIntegration.getReceivablesForFieldId(3)).
                thenReturn(
                        List.of(
                                new Receivable(3, "2020/2023", LocalDate.now(), "document-3", 3, LocalDate.now())
                        )
                );
        when(compositeIntegration.getReceivablesForFieldId(88)).
                thenReturn(
                        Collections.<Receivable>emptyList()
                );
    }

    @Test
    public void getStatusOfWSs() {
        System.out.println("test.");
        client.get().uri("/upstream-status")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MicroServiceStatus.class);
    }

    @Test
    public void getAllFields() throws JsonProcessingException {
        EntityExchangeResult<byte[]> cf = client.get().uri("/field-composite/fields")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody().returnResult();

        assertNotNull(cf.getResponseBody());

        String str = new String(cf.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Field[] fields = objectMapper.readValue(str, Field[].class);

        assertEquals(fields.length, 3);
    }

    @Test
    public void testGetCompositeField() throws JsonProcessingException {
        EntityExchangeResult<byte[]> cf = client.get().uri("/field-composite/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody().returnResult();

        assertNotNull(cf.getResponseBody());

        String str = new String(cf.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CompositeField compositeField = objectMapper.readValue(str, CompositeField.class);

        assertEquals(compositeField.getField().getIngenioId(), "oinky-ingenio-id-1");
        assertEquals(compositeField.getPayables().size(), 2);
        assertEquals(compositeField.getReceivables().size(), 3);
    }

    @Test
    public void testGetCompositeFieldNonExistent() {
        client.get().uri("/field-composite/88")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testCreateCompositeField() {

        CompositeField body = new CompositeField(
                new Field(1, "oinky-1", "oinky_the_owner-1", 1.0, 1.0, LocalDate.now().minusDays(1), "oinky-ingenio-id-1", LocalDate.now().minusDays(1)),
                List.of(new Payable(1, LocalDate.now().minusDays(1), 1.0, 1.0, 1, 1, "Category-1", "SubCategory-1", "Comment-1", LocalDate.now())),
                List.of(new Receivable(1, "2020/2021", LocalDate.now(), "document-1", 1, LocalDate.now()))
        );

        client.post().uri("/field-composite")
                .accept(APPLICATION_JSON)
                .body(Mono.just(body), CompositeField.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testGetAllCompositeField() throws JsonProcessingException {
        EntityExchangeResult<byte[]> cf = client.get().uri("/field-composite")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody().returnResult();

        assertNotNull(cf.getResponseBody());

        String str = new String(cf.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CompositeField[] compositeFields = objectMapper.readValue(str, CompositeField[].class);

        assertEquals(compositeFields.length, 3);
        assertEquals(compositeFields[1].getReceivables().size(), 0);
        assertEquals(compositeFields[1].getPayables().size(), 0);
        assertEquals(compositeFields[2].getReceivables().size(), 1);
        assertEquals(compositeFields[2].getReceivables().size(), 1);
    }
}
