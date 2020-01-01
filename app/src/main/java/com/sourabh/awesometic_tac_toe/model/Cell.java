package com.sourabh.awesometic_tac_toe.model;

public class Cell {

    private Player value;

    Player getValue() {
        return value;
    }

    void setValue(Player value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null || value.toString().length() != 0;
    }
}
