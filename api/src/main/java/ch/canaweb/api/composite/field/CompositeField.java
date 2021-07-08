package ch.canaweb.api.composite.field;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;

import java.util.List;

public class CompositeField {
    private Field field;
    private List<Payable> payables;
    private List<Receivable> receivables;

    public CompositeField() {
    }

    public CompositeField(Field field, List<Payable> payables, List<Receivable> receivables) {
        this.field = field;
        this.payables = payables;
        this.receivables = receivables;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<Payable> getPayables() {
        return payables;
    }

    public void setPayables(List<Payable> payables) {
        this.payables = payables;
    }

    public List<Receivable> getReceivables() {
        return receivables;
    }

    public void setReceivables(List<Receivable> receivables) {
        this.receivables = receivables;
    }
}
