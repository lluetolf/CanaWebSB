package ch.canaweb.api.core.Document;

import java.util.Date;

public class Document {
    private int id;
    private String name;
    private String comment;
    private String fileName;
    private Date lastUpdated;

    public Document(String name, String comment, String fileName, Date lastUpdated) {
        this.name = name;
        this.comment = comment;
        this.fileName = fileName;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
