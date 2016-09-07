package com.example.root.googlemaps.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.root.googlemaps.Adapter.InfoWindowAdapter;
import com.example.root.googlemaps.Bus;
import com.example.root.googlemaps.Class.GMapV2DirectionAsyncTask;
import com.example.root.googlemaps.Line;
import com.example.root.googlemaps.MainActivity;
import com.example.root.googlemaps.R;
import com.example.root.googlemaps.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by root on 06-07-2016.
 */
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {


    private MapView mapView;
    private GoogleMap googleMap;
    private Line currentLine;

    private float currentZoom;
    private ProgressDialog progressDialog;

    AsyncTask task;
    ArrayList<Polyline> polyLines;
    Marker currentPositionMarker;

    FirebaseDatabase db;
    DatabaseReference lineref;
    CircleOptions circleOptions;

    LinkedHashMap<String, String> busMap;
    public MapFragment() {    }

    private ThreadPoolExecutor mPool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments().getSerializable("currentLine") != null) {
            busMap = new LinkedHashMap<>();


            currentLine = (Line)getArguments().getSerializable("currentLine");

            final int red = Color.red(currentLine.getLightColor());
            final int green = Color.green(currentLine.getLightColor());
            final int blue = Color.blue(currentLine.getLightColor());
            final int color = Color.argb(150, red, green, blue);

            circleOptions = new CircleOptions();
            circleOptions.fillColor(color);
            circleOptions.strokeColor(Color.WHITE);
            circleOptions.strokeWidth(2);
            circleOptions.radius(30);


            if(currentLine.getBuses() != null) {
                db = FirebaseDatabase.getInstance();
                lineref = db.getReference("linhas").child(currentLine.getName()).child("autocarros");

                for(Bus bus : currentLine.getBuses())
                    busMap.put(bus.getId(), "true");
                lineref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String key = dataSnapshot.getKey();
                        if(!busMap.containsKey(key)) {
                            Bus bus = new Bus(dataSnapshot.child("lat").getValue(Double.class),
                                    dataSnapshot.child("long").getValue(Double.class));
                            bus.setId(dataSnapshot.getKey());

                            addBus(bus);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        for(Bus bus : currentLine.getBuses()) {
                            if(bus.getId().equals(dataSnapshot.getKey())) {
                                bus.setX(dataSnapshot.child("lat").getValue(Double.class));
                                bus.setY(dataSnapshot.child("long").getValue(Double.class));

                                if(bus.getMarker() != null)
                                    bus.getMarker().setCenter(new LatLng(bus.getX(), bus.getY()));
                                break;
                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getKey();

                        for(Bus bus : currentLine.getBuses()) {
                            if(bus.getId().equals(key)) {
                                removeBus(bus);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mapView == null) {
            mapView = (MapView) view.findViewById(R.id.map);
            if(getUserVisibleHint())
                loadMap();
        }
    }

    public void loadMap() {
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
        }
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        currentZoom = 14.4f;
        googleMap.clear();
        LatLng initialLatLang = new LatLng(currentLine.getInitialCoords()[0], currentLine.getInitialCoords()[1]);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(initialLatLang)
                .zoom(currentZoom)
                .tilt(40)
                .build();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        HashMap<String, Stop> stopsMap = new HashMap<>();
        for(Stop stop : currentLine.getStops()) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(stop.getX(), stop.getY()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop_blue))
            );
            stopsMap.put(marker.getId(), stop);
            googleMap.setOnMarkerClickListener(this);

            stopsMap.put(stop.getStreetName(), stop);
        }

        if(currentLine.getBuses() != null) {
            for(Bus bus : currentLine.getBuses()) {
                addBus(bus);
            }
        }


        final InfoWindowAdapter adapter = new InfoWindowAdapter(getContext(), stopsMap);
        googleMap.setInfoWindowAdapter(adapter);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Stop stop = adapter.getStop(marker.getId());
                if(stop != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.single_alert_horarios, null);

                    ListView listView = (ListView) view.findViewById(R.id.lv_horarios);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1,
                            stop.getHorarios());
                    listView.setAdapter(adapter);

                    builder.setView(view);
                    builder.setPositiveButton("Fechar", null);
                    builder.setTitle("Horário");
                    builder.create().show();
                }

            }
        });
        route(currentLine.getStops(), GMapV2DirectionAsyncTask.MODE_DRIVING);

        ((MainActivity)getActivity()).toggleLoad(false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void addZoom(float zoom) {
        if(currentZoom < 15.2 || zoom < 0) {
            currentZoom += zoom;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
        }
    }

    protected void route(ArrayList<Stop> stops, String mode) {
        final int red = Color.red(currentLine.getNormalColor());
        final int green = Color.green(currentLine.getNormalColor());
        final int blue = Color.blue(currentLine.getNormalColor());
        polyLines = new ArrayList<>();

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    PolylineOptions rectLine = (PolylineOptions)msg .obj;
                    rectLine.width(10).color(Color.argb(170, red, green, blue));
                    polyLines.add(googleMap.addPolyline(rectLine));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        new GMapV2DirectionAsyncTask(handler, stops).execute();

    }


    public void refreshMap() {
        for(Polyline line : polyLines) {
            line.remove();
        }
        route(currentLine.getStops(), GMapV2DirectionAsyncTask.MODE_DRIVING);
    }

    public void toggleLocation(LatLng latLng) {
        if(currentPositionMarker == null) {
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.icon(BitmapDescriptorFactory.defaultMarker());
            currentPositionMarker = googleMap.addMarker(options);
        }
        else {
            currentPositionMarker.remove();
            currentPositionMarker = null;
        }
    }

    private void addBus(Bus bus) {
        circleOptions.center(new LatLng(bus.getX(), bus.getY()));
        bus.setMarker (googleMap.addCircle(circleOptions));
        busMap.put(bus.getId(), "true");

        if(!currentLine.getBuses().contains(bus))
            currentLine.addBus(bus);
    }

    private void removeBus(Bus bus) {
        bus.getMarker().remove();
        bus.getMarker().setVisible(false);

        busMap.remove(bus.getId());

        currentLine.removeBus(bus);
    }
}
