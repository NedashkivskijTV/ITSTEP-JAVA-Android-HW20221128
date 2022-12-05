package edu.itstep.hw20221119.models;

public enum PizzaTopping {
    MEAT(15),
    MUSHROOMS(10),
    CHEESE(5);

    private int cost;

    PizzaTopping(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
