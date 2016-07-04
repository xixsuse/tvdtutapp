package com.example.root.googlemaps;

import java.io.Serializable;

/**
 * Created by root on 03-07-2016.
 */
public class Bus implements Serializable {
    private String name;
    private double x;
    private double y;

    public Bus() {}

    public Bus(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
