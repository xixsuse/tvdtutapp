package com.example.root.googlemaps.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.root.googlemaps.MainActivity;
import com.example.root.googlemaps.R;

public class BottomBarFragment extends Fragment implements View.OnClickListener {


    ImageButton btAddZoom;
    ImageButton btLessZoom;
    ImageButton btFindLoc;
    ImageButton btRefresh;

    MainActivity activity;
    public BottomBarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        btAddZoom = (ImageButton) v.findViewById(R.id.bt_add_zoom);
        btAddZoom.setOnClickListener(this);
        btLessZoom = (ImageButton) v.findViewById(R.id.bt_less_zoom);
        btLessZoom.setOnClickListener(this);
        btFindLoc = (ImageButton) v.findViewById(R.id.bt_find_loc);
        btFindLoc.setOnClickListener(this);

        btRefresh = (ImageButton) v.findViewById(R.id.bt_refresh_map);
        btRefresh.setOnClickListener(this);

        activity = (MainActivity)getActivity();
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btAddZoom.getId()) {
            activity.addZoom(0.2f);
        }
        else if(view.getId() == btLessZoom.getId()) {
            activity.addZoom(-0.2f);
        }
        else if(view.getId() == btFindLoc.getId()) {
            activity.toggleLocation();
        }
        else if(view.getId() == btRefresh.getId()) {
            activity.refreshMap();
        }
    }
}
