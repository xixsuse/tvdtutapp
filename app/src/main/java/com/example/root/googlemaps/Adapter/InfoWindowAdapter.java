package com.example.root.googlemaps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.googlemaps.R;
import com.example.root.googlemaps.Stop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by root on 05-07-2016.
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    HashMap<String, Stop> stops;

    public InfoWindowAdapter(Context context, HashMap<String, Stop> stops) {
        this.context = context;
        this.stops = stops;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.single_infowindow, null);

        if(stops.containsKey(marker.getId())) {
            Stop stop = stops.get(marker.getId());

            TextView txtStreet = (TextView) v.findViewById(R.id.txt_street_name);
            ImageView imgStreet = (ImageView) v.findViewById(R.id.img_rua);

            if(stop.getStreetName() != null)
                txtStreet.setText(stop.getStreetName());
            else
                txtStreet.setText(" ");
            imgStreet.setImageBitmap(stop.getImage());
            return v;
        }
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    public Stop getStop(String marker) {
        if(stops.containsKey(marker))
            return stops.get(marker);
        return null;
    }
}
