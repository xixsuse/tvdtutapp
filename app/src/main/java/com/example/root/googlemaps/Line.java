package com.example.root.googlemaps;

import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 03-07-2016.
 */
public class Line implements Serializable {
    private int id;
    private String name;
    private transient BitmapDrawable background;
    private double[] initialCoords;
    private ArrayList<Stop> stops;
    private ArrayList<Bus> busses;
    private int colorNormal;
    private int colorLight;

    public Line() {}

    public Line(String name, int colorNormal, int colorLight, double[] initialCoords) {
        this.name = name;
        this.initialCoords = initialCoords;
        this.colorNormal = colorNormal;
        this.colorLight = colorLight;
        stops = new ArrayList<>();
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

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    public void setBuses(ArrayList<Bus> buses) {
        this.busses = buses;
    }

    public ArrayList<Bus> getBuses() {
        return busses;
    }

    public BitmapDrawable getBackground() {
        return background;
    }

    public void setBackground(BitmapDrawable background) {
        this.background = background;
    }

    public int getNormalColor() {
        return colorNormal;
    }

    public void setNormalColor(int color) {
        this.colorNormal = color;
    }

    public int getLightColor() {
        return colorLight;
    }

    public void setLightColor(int color) {
        this.colorLight = color;
    }

    public void addStop(Stop stop) {
        if(stop != null) {
            stops.add(stop);
        }
    }

    public void addBus(Bus bus) {
        busses.add(bus);
    }

    public void removeBus(Bus bus) {
        busses.remove(bus);
    }
}
