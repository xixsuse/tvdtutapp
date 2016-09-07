package com.example.root.googlemaps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.googlemaps.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by root on 07-07-2016.
 */
public class DrawerAdapter extends BaseAdapter {

    Context context;
    LinkedHashMap<String, Integer> items;


    public DrawerAdapter(Context context, LinkedHashMap<String, Integer> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(i > 0) {
            i -= 1;
            view = inflater.inflate(R.layout.single_row_drawer, viewGroup, false);
            TextView txtName = (TextView) view.findViewById(R.id.txt_name);
            ImageView imgIcon = (ImageView) view.findViewById(R.id.img_icon);

            txtName.setText(items.keySet().toArray()[i].toString());
            imgIcon.setImageResource(new ArrayList<Integer>(items.values()).get(i));
        }
        else {
            view = inflater.inflate(R.layout.single_row_drawer_header, viewGroup, false);
        }
        return view;
    }

    @Override
    public int getCount() {
        return (items.size())+1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0)
            return null;
        return items.get((new ArrayList<Integer>(items.values())).get(i-1));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
