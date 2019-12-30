package com.sourabh.awesometic_tac_toe.viewmodel;

public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    //Prevents the data being called again
    public T getContentIfNotHandled() {

        if (hasBeenHandled) { //Return null if true
            return null;
        } else {
            hasBeenHandled = true;  //Set to true and return the data
            return content;
        }
    }

    public boolean isHandled() {
        return hasBeenHandled;
    }

    //Returns content even if it has been handled
    public T peek() {
        return content;
    }
}
