package com.example.root.googlemaps.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.root.googlemaps.MapsActivity;
import com.example.root.googlemaps.R;

public class BottomBarFragment extends Fragment implements View.OnClickListener {


    ImageButton btAddZoom;
    ImageButton btLessZoom;
    ImageButton btFindLoc;

    public BottomBarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        btAddZoom = (ImageButton) v.findViewById(R.id.bt_add_zoom);
        btAddZoom.setOnClickListener(this);
        btLessZoom = (ImageButton) v.findViewById(R.id.bt_less_zoom);
        btLessZoom.setOnClickListener(this);
        btFindLoc = (ImageButton) v.findViewById(R.id.bt_find_loc);
        btFindLoc.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btAddZoom.getId()) {
            ((MapsActivity)getActivity()).addZoom(0.2f);
        }
        else if(view.getId() == btLessZoom.getId()) {
            ((MapsActivity)getActivity()).addZoom(-0.2f);
        }
        else if(view.getId() == btFindLoc.getId()) {
            ((MapsActivity)getActivity()).showCurrentLocation(true);
        }
    }
}
