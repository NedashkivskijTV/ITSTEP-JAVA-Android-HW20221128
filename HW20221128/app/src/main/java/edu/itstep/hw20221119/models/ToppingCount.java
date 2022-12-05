package edu.itstep.hw20221119.models;

import java.io.Serializable;

public class ToppingCount implements Serializable {
    private PizzaTopping pizzaTopping;
    private int count;

    public ToppingCount() {
    }

    public ToppingCount(PizzaTopping pizzaTopping, int count) {
        this.pizzaTopping = pizzaTopping;
        this.count = count;
    }

    public PizzaTopping getPizzaTopping() {
        return pizzaTopping;
    }

    public void setPizzaTopping(PizzaTopping pizzaTopping) {
        this.pizzaTopping = pizzaTopping;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
