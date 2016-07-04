package com.example.root.googlemaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Line blueLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        blueLine = new Line("Blue Line", new double[] {39.091776, -9.260034});
        blueLine.setStops(new Stop[]{
            new Stop(39.094089, -9.257458),
            new Stop(39.093858, -9.254939),
            new Stop(39.095492, -9.252359),
            new Stop(39.097959, -9.253250),
            new Stop(39.100777, -9.254339),
            new Stop(39.100640, -9.259580),
            new Stop(39.105187, -9.259015),
            new Stop(39.106619, -9.261697),
            new Stop(39.106810, -9.265613),
            new Stop(39.104857, -9.265700),
        });
        blueLine.setBuses(new Bus[] {
            new Bus(39.098789, -9.260383),
            new Bus(39.094029, -9.257199),
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLatLang = new LatLng(blueLine.getInitialCoords()[0], blueLine.getInitialCoords()[1]);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(initialLatLang)
                .zoom(15)
                .tilt(30)
                .build();

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLang, 16));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        int i = 0;
        for(Stop stop : blueLine.getStops()) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(stop.getX(), stop.getY()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_blue))
            );
            if(i < blueLine.getStops().length-1) {
                final int finalI = i;
                new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                           GMapV2Direction md = new GMapV2Direction();
                           Document doc = md.getDocument(
                               new LatLng(blueLine.getStops()[finalI].getX(), blueLine.getStops()[finalI].getY()),
                               new LatLng(blueLine.getStops()[finalI +1].getX(), blueLine.getStops()[finalI +1].getY()),
                               GMapV2Direction.MODE_DRIVING);
                           ArrayList<LatLng> directionPoint = md.getDirection(doc);
                           PolylineOptions rectLine = new PolylineOptions().width(10).color(
                           Color.argb(200, 20, 20, 150));
                           for (int i = 0; i < directionPoint.size(); i++) {
                               rectLine.add(directionPoint.get(i));
                           }
                           drawPolyline(rectLine);
                        }
                    }
                ).start();
            }
            i++;
        }

        for(Bus bus : blueLine.getBuses()) {
            mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot_30x30))
                .flat(true)
                .position(new LatLng(bus.getX(), bus.getY()))
            );
        }
    }

    public void drawPolyline(final PolylineOptions rectLine) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addPolyline(rectLine);
            }
        });

    }

}
