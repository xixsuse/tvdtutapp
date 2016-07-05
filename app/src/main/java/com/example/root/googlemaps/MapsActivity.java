package com.example.root.googlemaps;


import android.app.Activity;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.root.googlemaps.Adapter.InfoWindowAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private Line currentLine;
    private float currentZoom;
    private Marker currentMarker;

    private double currentLatitude;
    private double currentLongitude;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        overridePendingTransition(R.transition.fadein, R.transition.fadeout);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            currentLine = (Line) bundle.getSerializable("currentLine");
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
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

    public void addZoom(float zoom) {
        /*if(currentZoom < 15.2 || zoom < 0) {*/
            currentZoom += zoom;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
        /*}*/
    }

    public void showCurrentLocation(boolean b) {
        if(b) {
            mGoogleApiClient.connect();
            if(mGoogleApiClient.isConnected())
            {
                if(isShowingLocation())
                    destroyLocationMarker();

                currentMarker = mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(currentLatitude, currentLongitude))
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
                final GMapV2Direction md = new GMapV2Direction();
                new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            showCurrentLocation(!isShowingLocation());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
}
