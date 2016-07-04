package com.example.root.googlemaps;

/**
 * Created by root on 03-07-2016.
 */
public class Stop {

    private int id;
    private double x;
    private double y;

    public Stop(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
