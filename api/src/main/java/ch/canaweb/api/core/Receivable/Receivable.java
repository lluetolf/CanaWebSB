package ch.canaweb.api.core.Receivable;

import java.util.Date;

public class Receivable {
    private int id;
    private Date transactionDate;
    private String documentId;
    private int fieldId;
    private Date lastUpdated;

    public Receivable(Date transactionDate, String documentId, int fieldId, Date lastUpdated) {
        this.transactionDate = transactionDate;
        this.documentId = documentId;
        this.fieldId = fieldId;
        this.lastUpdated = lastUpdated;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
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

    public int getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
