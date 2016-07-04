package com.example.root.googlemaps;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.root.googlemaps.Adapter.HorarioAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    Line blueLine;
    Line redLine;
    Line yellowLine;

    ArrayList<Line> lines;
    HorarioAdapter adapter;
    GridView gvMain;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gvMain = (GridView) findViewById(R.id.gv_main);
        blueLine = new Line("Linha Azul", Color.rgb(51, 153, 255), Color.rgb(51, 133, 255), new double[] {39.091776, -9.260034});
        blueLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));
        blueLine.setStops(new Stop[]{
                new Stop(39.094089, -9.257458),
                new Stop(39.093858, -9.254939),
                new Stop(39.095492, -9.252359),
                new Stop(39.097959, -9.253250),
                new Stop(39.100777, -9.254339),
                new Stop(39.100640, -9.259580),
                new Stop(39.105187, -9.259015),
                new Stop(39.106619, -9.261697),
                new Stop(39.106810, -9.265613),
                new Stop(39.104857, -9.265700),
        });
        blueLine.setBuses(new Bus[] {
                new Bus(39.098789, -9.260383),
                new Bus(39.094029, -9.257199),
        });

        redLine = new Line("Linha Vermelha", Color.rgb(255, 26, 26), Color.rgb(51, 133, 255), new double[] {0, 0});
        redLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        yellowLine= new Line("Linha Amarela", Color.rgb(255, 204, 0), Color.rgb(51, 133, 255), new double[] {0, 0});
        yellowLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        lines = new ArrayList<>();
        lines.add(blueLine);
        lines.add(redLine);
        lines.add(yellowLine);
        adapter = new HorarioAdapter(this, lines);
        gvMain.setAdapter(adapter);

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    openMaps(null);
                }
            }
        });
    }

    public void openMaps(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("currentLine", blueLine);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
