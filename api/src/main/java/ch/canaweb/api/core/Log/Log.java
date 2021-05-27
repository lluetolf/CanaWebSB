package ch.canaweb.api.core.Log;

import java.util.Date;

public class Log {
    private int id;
    private String source;
    private String message;
    private Date datetime;

    public Log(String source, String message, Date datetime) {
        this.source = source;
        this.message = message;
        this.datetime = datetime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }
}
