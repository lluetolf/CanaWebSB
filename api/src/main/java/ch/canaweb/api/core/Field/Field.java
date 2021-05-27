package ch.canaweb.api.core.Field;

import java.util.Date;

public class Field {
    private int fieldId;
    private String name;
    private String owner;
    private double size;
    private double cultivatedArea;
    private Date acquisitionDate;
    private String ingenioId;
    private Date lastUpdated;

    public Field(String name, String owner, double size, double cultivatedArea, Date acquisitionDate, String ingenioId, Date lastUpdated) {
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

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getIngenioId() {
        return ingenioId;
    }

    public void setIngenioId(String ingenioId) {
        this.ingenioId = ingenioId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
