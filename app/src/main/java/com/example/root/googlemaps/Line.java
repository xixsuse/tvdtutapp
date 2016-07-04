package com.example.root.googlemaps;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by root on 03-07-2016.
 */
public class Line {
    private int id;
    private String name;
    private BitmapDrawable background;
    private double[] initialCoords;
    private Stop[] stops;
    private Bus[] buses;
    
    public Line() {}

    public Line(String name, double[] initialCoords) {
        this.name = name;
        this.initialCoords = initialCoords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getInitialCoords() {
        return initialCoords;
    }

    public void setInitialCoords(double[] initialCoords) {
        this.initialCoords = initialCoords;
    }

    public Stop[] getStops() {
        return stops;
    }

    public void setStops(Stop[] stops) {
        this.stops = stops;
    }

    public void setBuses(Bus[] buses) {
        this.buses = buses;
    }

    public Bus[] getBuses() {
        return buses;
    }

    public BitmapDrawable getBackground() {
        return background;
    }

    public void setBackground(BitmapDrawable background) {
        this.background = background;
    }
}
