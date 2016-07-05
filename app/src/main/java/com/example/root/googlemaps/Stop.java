package com.example.root.googlemaps;

import java.io.Serializable;

/**
 * Created by root on 03-07-2016.
 */
public class Stop implements Serializable {

    private int id;
    private double x;
    private double y;
    private String streetName;

    public Stop(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Stop(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.streetName = name;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
