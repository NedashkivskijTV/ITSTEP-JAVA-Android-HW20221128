package edu.itstep.hw20221119.models;

public enum PizzaSize {
    LARGE(100, 100.0),
    MEDIUM(60, 50.0),
    SMALL(30, 0);

    private int sizeSm;
    private double margin;

    PizzaSize(int sizeSm, double margin) {
        this.sizeSm = sizeSm;
        this.margin = margin;
    }

    public int getSizeSm() {
        return sizeSm;
    }

    public double getMargin() {
        return margin;
    }
}
