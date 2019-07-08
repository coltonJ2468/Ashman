package com.example.colto.cjohnsonfinal;

import java.io.Serializable;

class Circle implements Serializable {

    private float locX;
    private float locY;
    private int direction;
    private boolean ifMoved;

    Circle(float locX, float locY, int direction) {
        this.locX = locX;
        this.locY = locY;
        this.direction = direction;
        this.ifMoved = false;
    }

    float getLocX() {
        return this.locX;
    }

    float getLocY() {
        return this.locY;
    }

    int getDirection() {
        return this.direction;
    }

    boolean getIfMoved() {
        return this.ifMoved;
    }

    void setLocX(float x) {
        this.locX = x;
    }

    void setLocY(float y) {
        this.locY = y;
    }

    void setDirection(int direction) {
        this.direction = direction;
    }

    void setIfMoved(boolean ifMoved) {
        this.ifMoved = ifMoved;
    }
}
