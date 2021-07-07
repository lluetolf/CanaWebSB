package ch.canaweb.microservices.core.field.persistence;

import ch.canaweb.api.core.Field.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection="fields")
public class FieldEntity extends Field {
    @Id
    private String id;

    @Version
    private Integer version;

    public FieldEntity() {
    }

    public FieldEntity(int fieldId, String name, String owner, double size, double cultivatedArea, LocalDate acquisitionDate, String ingenioId, LocalDate lastUpdated) {
        super(fieldId, name, owner, size, cultivatedArea, acquisitionDate, ingenioId, lastUpdated);
    }

    public String getId() {
        return id;
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
