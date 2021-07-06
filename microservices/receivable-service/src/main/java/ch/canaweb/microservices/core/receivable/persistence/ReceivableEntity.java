package ch.canaweb.microservices.core.receivable.persistence;

import ch.canaweb.api.core.Receivable.Receivable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;


@Document(collection="receivables")
public class ReceivableEntity extends Receivable {
    @Id
    private String id;

    @Version
    private Integer version;

    public ReceivableEntity() {
    }

    public ReceivableEntity(LocalDate transactionDate, String documentId, int fieldId, LocalDate lastUpdated) {
        super(transactionDate, documentId, fieldId, lastUpdated);
    }

    public String getId() {
        return id + "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
