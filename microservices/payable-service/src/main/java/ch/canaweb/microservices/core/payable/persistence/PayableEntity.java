package ch.canaweb.microservices.core.payable.persistence;

import ch.canaweb.api.core.Payable.Payable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection="payables")
public class PayableEntity extends Payable {
    @Id
    private String id;

    @Version
    private Integer version;

    public PayableEntity(int payableId, Date transactionDate, double pricePerUnit, double quantity, int documentId, int fieldId, String category, String subCategory, String comment, Date lastUpdated) {
        super(payableId, transactionDate, pricePerUnit, quantity, documentId, fieldId, category, subCategory, comment, lastUpdated);
    }

    public String getId() {
        return Objects.requireNonNull(id);
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
