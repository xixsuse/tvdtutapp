package com.example.root.googlemaps;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

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
    private Line currentLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            currentLine = (Line) bundle.getSerializable("currentLine");
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLatLang = new LatLng(currentLine.getInitialCoords()[0], currentLine.getInitialCoords()[1]);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(initialLatLang)
                .zoom(15)
                .tilt(30)
                .build();

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLang, 16));

        int i = 0;
        final int red = Color.red(currentLine.getLightColor());
        final int green = Color.green(currentLine.getLightColor());
        final int blue = Color.blue(currentLine.getLightColor());
        final int color = Color.argb(150, red, green, blue);

        for(Stop stop : currentLine.getStops()) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(stop.getX(), stop.getY()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_blue))
            );
            if(i < currentLine.getStops().length-1) {
                final int finalI = i;
                new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                           GMapV2Direction md = new GMapV2Direction();
                           Document doc = md.getDocument(
                               new LatLng(currentLine.getStops()[finalI].getX(), currentLine.getStops()[finalI].getY()),
                               new LatLng(currentLine.getStops()[finalI +1].getX(), currentLine.getStops()[finalI +1].getY()),
                               GMapV2Direction.MODE_DRIVING);
                           ArrayList<LatLng> directionPoint = md.getDirection(doc);


                           PolylineOptions rectLine = new PolylineOptions().width(10).color(color);
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

        for(Bus bus : currentLine.getBuses()) {
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
