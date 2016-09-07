package com.example.root.googlemaps;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

/**
 * Created by root on 03-07-2016.
 */
public class Bus implements Serializable {
    private String name;
    private double x;
    private double y;
    private Circle marker;
    private String id;

    public Bus() {}

    public Bus(double x, double y) {
        this.x = x;
        this.y = y;
        this.marker = null;
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

    public Circle getMarker() {
        return marker;
    }

    public void setMarker(Circle marker) {
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
