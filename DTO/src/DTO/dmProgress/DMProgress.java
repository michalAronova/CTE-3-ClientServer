package DTO.dmProgress;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class DMProgress implements Serializable {
    private final double total;
    private final double produced;
    private final double completed;

    public DMProgress(double total, double produced, double completed) {
        this.total = total;
        this.produced = produced;
        this.completed = completed;
    }

    public double getTotal() {
        return total;
    }

    public double getProduced() {
        return produced;
    }

    public double getCompleted() {
        return completed;
    }
}
