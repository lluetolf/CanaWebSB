package ch.canaweb.api.core.Payable;

import java.time.LocalDate;

public class Payable {
    private int payableId;
    private LocalDate transactionDate;
    private double pricePerUnit;
    private double quantity;
    private int documentId;
    private int fieldId;
    private String category;
    private String subCategory;
    private String comment;
    private LocalDate lastUpdated;

    public Payable() {
    }

    public Payable(int payableId, LocalDate transactionDate, double pricePerUnit, double quantity, int documentId, int fieldId, String category, String subCategory, String comment, LocalDate lastUpdated) {
        this.payableId = payableId;
        this.transactionDate = transactionDate;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.documentId = documentId;
        this.fieldId = fieldId;
        this.category = category;
        this.subCategory = subCategory;
        this.comment = comment;
        this.lastUpdated = lastUpdated;
    }

    public int getPayableId() {
        return payableId;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPayableId(int payableId) {
        this.payableId = payableId;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

