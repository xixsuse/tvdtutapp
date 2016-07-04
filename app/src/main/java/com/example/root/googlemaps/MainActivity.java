package com.example.root.googlemaps;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.root.googlemaps.Adapter.HorarioAdapter;

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
        blueLine = new Line("Linha Azul", new double[] {0, 0});
        blueLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        redLine = new Line("Linha Vermelha", new double[] {0, 0});
        redLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        yellowLine= new Line("Linha Amarela", new double[] {0, 0});
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
        startActivity(intent);
    }
}
