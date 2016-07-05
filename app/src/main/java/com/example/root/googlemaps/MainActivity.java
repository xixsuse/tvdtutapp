package com.example.root.googlemaps;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.root.googlemaps.Adapter.HorarioAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    Line blueLine;
    Line redLine;
    Line yellowLine;
    Line greenLine;

    ArrayList<Line> lines;
    HorarioAdapter adapter;
    GridView gvMain;

    Toolbar toolbar;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    ArrayAdapter<String> drawerAdapter;

    ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.transition.fadein, R.transition.fadeout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerList = (ListView)findViewById(R.id.navList);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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
                new Stop(39.104338, -9.263414),
                new Stop(39.103568, -9.259492),
                new Stop(39.100591, -9.259948),
                new Stop(39.100729, -9.254289),
                new Stop(39.097831, -9.253291),
                new Stop(39.093963, -9.255308),
                new Stop(39.094147, -9.257449),

                new Stop(39.092288, -9.255516),
                new Stop(39.091179, -9.258568),
                new Stop(39.088799, -9.257906),
                new Stop(39.087522, -9.259181),
                new Stop(39.089313, -9.261145),
                new Stop(39.090764, -9.261774),
                new Stop(39.091951, -9.265797),
        });
        blueLine.setBuses(new Bus[] {
                new Bus(39.098789, -9.260383),
                new Bus(39.094029, -9.257199),
        });

        redLine = new Line("Linha Vermelha", Color.rgb(255, 26, 26), Color.rgb(255, 26, 26), new double[] {39.091776, -9.260034});
        redLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        redLine.setStops(new Stop[]{
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
        redLine.setBuses(new Bus[] {
                new Bus(39.098789, -9.260383),
                new Bus(39.094029, -9.257199),
        });

        yellowLine= new Line("Linha Amarela", Color.rgb(255, 204, 0), Color.rgb(51, 133, 255), new double[] {0, 0});
        yellowLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        greenLine= new Line("Linha Verde", Color.rgb(0, 179, 0), Color.rgb(0, 204, 0), new double[] {0, 0});
        greenLine.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.img1));

        lines = new ArrayList<>();
        lines.add(blueLine);
        lines.add(redLine);
        lines.add(yellowLine);
        lines.add(greenLine);
        adapter = new HorarioAdapter(this, lines);
        gvMain.setAdapter(adapter);

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openMaps(i);
            }
        });
        addDrawerItems();
    }

    public void openMaps(int i) {

        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        Bundle bundle = new Bundle();

        if(i == 0)
            bundle.putSerializable("currentLine", blueLine);
        else if(i == 1)
            bundle.putSerializable("currentLine", redLine);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void addDrawerItems() {
        String[] osArray = {"Android", "iOS", "Windows", "OS X", "Linux"};
        drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        drawerList.setAdapter(drawerAdapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close)  {

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Opções");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }


}
