package com.example.root.googlemaps;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 03-07-2016.
 */
public class Stop implements Serializable {

    private int id;
    private double x;
    private double y;
    private String streetName;
    private ArrayList<String> horarios;
    private Bitmap image;

    public Stop(double x, double y) {
        this.x = x;
        this.y = y;
        this.streetName = null;
        this.horarios = new ArrayList<>();
    }

    public Stop(double x, double y, String name, ArrayList<String> horarios) {
        this.x = x;
        this.y = y;
        this.streetName = name;
        this.horarios = horarios;
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

    public ArrayList<String> getHorarios() { return horarios; }

    public void setHorarios(ArrayList<String>horarios ) { this.horarios = horarios; }

    public String getHorario(int i ) { return horarios.get(i); }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
