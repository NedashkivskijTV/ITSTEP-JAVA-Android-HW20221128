package edu.itstep.hw20221119.models;

public enum PizzaRecipe {
    PANCHETA_GORGONDZOLA(174),
    MEAT_PIZZA(195),
    PHILADELPHIA(146);

    private int cost;

    PizzaRecipe(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
