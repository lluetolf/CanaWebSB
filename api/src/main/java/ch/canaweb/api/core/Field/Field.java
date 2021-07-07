package ch.canaweb.api.core.Field;

import java.time.LocalDate;

public class Field {
    private int fieldId;
    private String name;
    private String owner;
    private double size;
    private double cultivatedArea;
    private LocalDate acquisitionDate;
    private String ingenioId;
    private LocalDate lastUpdated;

    public Field() {
    }

    public Field(int fieldId, String name, String owner, double size, double cultivatedArea, LocalDate acquisitionDate, String ingenioId, LocalDate lastUpdated) {
        this.fieldId = fieldId;
        this.name = name;
        this.owner = owner;
        this.size = size;
        this.cultivatedArea = cultivatedArea;
        this.acquisitionDate = acquisitionDate;
        this.ingenioId = ingenioId;
        this.lastUpdated = lastUpdated;
    }

    public int getFieldId() {
        return fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getCultivatedArea() {
        return cultivatedArea;
    }

    public void setCultivatedArea(double cultivatedArea) {
        this.cultivatedArea = cultivatedArea;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getIngenioId() {
        return ingenioId;
    }

    public void setIngenioId(String ingenioId) {
        this.ingenioId = ingenioId;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }
}
