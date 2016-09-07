package com.example.root.googlemaps.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.root.googlemaps.Fragment.MapFragment;
import com.example.root.googlemaps.Line;

import java.util.ArrayList;

public class HomePagerAdapter extends FragmentPagerAdapter {

    ArrayList<Line> lines;
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public HomePagerAdapter(FragmentManager fm, ArrayList<Line> lines) {
        super(fm);
        this.lines = lines;
    }

    @Override
    public Fragment getItem(int i) {
        //Fragment fragment = new MapFragment(lines.get(i));
        Fragment fragment = newInstance(lines.get(i));
        return fragment;
    }

    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Line line = lines.get(position);
        return "Linha " + line.getName();
    }

    public static final MapFragment newInstance(Line line)
    {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle(2);
        bundle.putSerializable("currentLine", line);
        fragment.setArguments(bundle);
        return fragment;
    }
}

