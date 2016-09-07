package com.example.root.googlemaps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.googlemaps.R;

import java.util.ArrayList;

/**
 * Created by root on 04-07-2016.
 */
public class HorarioAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> horarios;

    public HorarioAdapter(Context context, ArrayList<String> horarios) {
        this.context = context;
        this.horarios = horarios;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.single_row_horario, viewGroup, false);

        TextView txtHorario = (TextView) view.findViewById(R.id.txt_horario);
        txtHorario.setText(horarios.get(i));
        return view;
    }

    @Override
    public int getCount() {
        return horarios.size();
    }

    @Override
    public Object getItem(int i) {
        return horarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
