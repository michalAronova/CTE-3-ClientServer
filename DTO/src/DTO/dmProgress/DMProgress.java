package DTO.dmProgress;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DMProgress {
    private final DoubleProperty total;
    private final DoubleProperty produced;
    private final DoubleProperty completed;

    public DMProgress(double total, double produced, double completed) {
        this.total = new SimpleDoubleProperty(total);
        this.produced = new SimpleDoubleProperty(produced);
        this.completed = new SimpleDoubleProperty(completed);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public double getProduced() {
        return produced.get();
    }

    public DoubleProperty producedProperty() {
        return produced;
    }

    public double getCompleted() {
        return completed.get();
    }

    public DoubleProperty completedProperty() {
        return completed;
    }
}
