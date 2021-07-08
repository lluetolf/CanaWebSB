package ch.canaweb.api.composite.field;

public class MicroServiceStatus {
    private String fieldServiceStatus;
    private String payableServiceStatus;
    private String receivableServiceStatus;

    public MicroServiceStatus() {
    }

    public MicroServiceStatus(String fieldServiceStatus, String payableServiceStatus, String receivableServiceStatus) {
        this.fieldServiceStatus = fieldServiceStatus;
        this.payableServiceStatus = payableServiceStatus;
        this.receivableServiceStatus = receivableServiceStatus;
    }

    public String getFieldServiceStatus() {
        return fieldServiceStatus;
    }

    public void setFieldServiceStatus(String fieldServiceStatus) {
        this.fieldServiceStatus = fieldServiceStatus;
    }

    public String getPayableServiceStatus() {
        return payableServiceStatus;
    }

    public void setPayableServiceStatus(String payableServiceStatus) {
        this.payableServiceStatus = payableServiceStatus;
    }

    public String getReceivableServiceStatus() {
        return receivableServiceStatus;
    }

    public void setReceivableServiceStatus(String receivableServiceStatus) {
        this.receivableServiceStatus = receivableServiceStatus;
    }
}
