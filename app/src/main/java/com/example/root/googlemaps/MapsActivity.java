package com.example.root.googlemaps;


import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.root.googlemaps.Adapter.InfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Line currentLine;
    private float currentZoom;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        overridePendingTransition(R.transition.fadein, R.transition.fadeout);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            currentLine = (Line) bundle.getSerializable("currentLine");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void drawPolyline(final PolylineOptions rectLine) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addPolyline(rectLine);
            }
        });

    }

    public void addZoom(float zoom) {
        if(currentZoom < 15.2 || zoom < 0) {
            currentZoom += zoom;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
        }
    }

    public void showCurrentLocation(boolean b) {
        if(b) {
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                if(isShowingLocation())
                    destroyLocationMarker();
                currentMarker = mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                  .icon(BitmapDescriptorFactory.fromResource(R.drawable.white_marker))
                );
            }
        }
        else {
            destroyLocationMarker();
        }
    }

    public boolean isShowingLocation() {
        return ((currentMarker == null) ? false : true);
    }

    public void destroyLocationMarker() {
        if(currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
    }

    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentZoom = 15f;

        LatLng initialLatLang = new LatLng(currentLine.getInitialCoords()[0], currentLine.getInitialCoords()[1]);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(initialLatLang)
                .zoom(currentZoom)
                .tilt(30)
                .build();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        int i = 0;
        final int red = Color.red(currentLine.getLightColor());
        final int green = Color.green(currentLine.getLightColor());
        final int blue = Color.blue(currentLine.getLightColor());
        final int color = Color.argb(150, red, green, blue);


        HashMap<String, Stop> stopsMap = new HashMap<>();
        for(Stop stop : currentLine.getStops()) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(stop.getX(), stop.getY()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_blue))
                    .title("Paragem #" + i)
            );
            stopsMap.put(marker.getId(), stop);
            mMap.setOnMarkerClickListener(this);


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
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this, stopsMap));


        for(Bus bus : currentLine.getBuses()) {
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(bus.getX(), bus.getY()))
                    .radius(10).fillColor(currentLine.getLightColor()).strokeColor(Color.WHITE).strokeWidth(1);
            mMap.addCircle(circleOptions);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
