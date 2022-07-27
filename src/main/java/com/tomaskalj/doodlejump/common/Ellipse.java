package com.tomaskalj.doodlejump.common;

public class Ellipse {
    private float x;
    private float y;
    private float width;
    private float height;

    public Ellipse(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }


    public float getHeight() {
        return height;
    }

    public boolean contains(float otherX, float otherY) {
        return (Math.pow((x - otherX), 2) / Math.pow(width, 2)) + (Math.pow((y - otherY), 2) / Math.pow(height, 2)) <= 1;
    }
}
