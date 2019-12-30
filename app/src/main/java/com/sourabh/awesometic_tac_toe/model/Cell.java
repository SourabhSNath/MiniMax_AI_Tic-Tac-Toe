package com.sourabh.awesometic_tac_toe.model;

class Cell {
    private Player value;

//    Cell(Player currentPlayer) {
//        this.value = currentPlayer;
//    }

    Player getValue() {
        return value;
    }

    void setValue(Player value) {
        this.value = value;
    }
}
