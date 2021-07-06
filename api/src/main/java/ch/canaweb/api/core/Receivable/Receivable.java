package ch.canaweb.api.core.Receivable;

import java.time.LocalDate;

public class Receivable {
    private int receivableId;
    private String zafra;
    private LocalDate transactionDate;
    private String documentId;
    private int fieldId;
    private LocalDate lastUpdated;

    public Receivable() {
    }

    public Receivable(int receivableId, String zafra, LocalDate transactionDate, String documentId, int fieldId, LocalDate lastUpdated) {
        this.receivableId = receivableId;
        this.zafra = zafra;
        this.transactionDate = transactionDate;
        this.documentId = documentId;
        this.fieldId = fieldId;
        this.lastUpdated = lastUpdated;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getReceivableId() {
        return receivableId;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setReceivableId(int receivableId) {
        this.receivableId = receivableId;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getZafra() {
        return zafra;
    }

    public void setZafra(String zafra) {
        this.zafra = zafra;
    }
}
