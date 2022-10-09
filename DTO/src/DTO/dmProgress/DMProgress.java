package DTO.dmProgress;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DMProgress {
    private final IntegerProperty total;
    private final IntegerProperty produced;
    private final IntegerProperty completed;

    public DMProgress(int total, int produced, int completed) {
        this.total = new SimpleIntegerProperty(total);
        this.produced = new SimpleIntegerProperty(produced);
        this.completed = new SimpleIntegerProperty(completed);
    }

    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public int getProduced() {
        return produced.get();
    }

    public IntegerProperty producedProperty() {
        return produced;
    }

    public int getCompleted() {
        return completed.get();
    }

    public IntegerProperty completedProperty() {
        return completed;
    }
}
