package com.example.root.googlemaps.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.googlemaps.Line;
import com.example.root.googlemaps.R;

import java.util.ArrayList;

/**
 * Created by root on 04-07-2016.
 */
public class HorarioAdapter extends BaseAdapter {

    Context context;
    ArrayList<Line> lines;

    public HorarioAdapter(Context context, ArrayList<Line> lines) {
        this.context = context;
        this.lines = lines;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.single_row_horario, viewGroup, false);

        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 400));
        Line line = lines.get(i);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_hor_name);
        ImageView imgBackground = (ImageView) view.findViewById(R.id.iv_background);
        Typeface titleFont = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat_Bold.ttf");

        imgBackground.setImageBitmap(line.getBackground().getBitmap());
        txtTitle.setText(line.getName());
        txtTitle.setTypeface(titleFont);
        return view;
    }

    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public Object getItem(int i) {
        return lines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
