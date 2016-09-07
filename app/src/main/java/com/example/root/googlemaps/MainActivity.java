package com.example.root.googlemaps;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.root.googlemaps.Adapter.DrawerAdapter;
import com.example.root.googlemaps.Adapter.HomePagerAdapter;
import com.example.root.googlemaps.Fragment.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class MainActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    ArrayList<Line> lines;

    Toolbar toolbar;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    DrawerAdapter drawerAdapter;
    ViewPager pager;
    ShareActionProvider shareActionProvider;
    HomePagerAdapter homePagerAdapter;
    ProgressBar pbLoad;

    FirebaseDatabase db;
    DatabaseReference linesref;
    public static FragmentManager fragmentManager;
    PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.transition.fadein, R.transition.fadeout);
        fragmentManager = getSupportFragmentManager();

        db = FirebaseDatabase.getInstance();
        linesref = db.getReference("linhas");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerList = (ListView)findViewById(R.id.navList);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        linesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    Line line = new Line(snap.getKey(),
                            snap.child("colorNormal").getValue(Integer.class),
                            snap.child("colorLight").getValue(Integer.class),
                            new double[] {39.091776, -9.260034});

                    ArrayList<Bus> busses = new ArrayList<>();
                    for(DataSnapshot bussnap : snap.child("autocarros").getChildren()) {
                        Bus bus = new Bus(bussnap.child("lat").getValue(Double.class),
                                bussnap.child("long").getValue(Double.class));
                        bus.setId(bussnap.getKey());
                        busses.add(bus);
                    }
                    for(DataSnapshot stopsnap : snap.child("paragens").getChildren()) {
                        Stop stop = new Stop(
                                stopsnap.child("lat").getValue(Double.class),
                                stopsnap.child("long").getValue(Double.class));

                        if(stopsnap.hasChild("img")) {
                            byte[] img = Base64.decode(stopsnap.child("img").getValue(String.class), Base64.DEFAULT);
                            stop.setImage(BitmapFactory.decodeByteArray(img, 0, img.length));
                        }

                        if(stopsnap.hasChild("name")) {
                            stop.setStreetName(stopsnap.child("name").getValue(String.class));
                        }
                        line.addStop(stop);
                    }
                    line.setBuses(busses);
                    lines.add(line);
                }
                startTabsAndFragments();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lines = new ArrayList<>();
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setIndicatorHeight(7);
        tabs.setTextColorResource(R.color.light_font);
        tabs.setDividerColorResource(R.color.colorLightGray);
        tabs.setIndicatorColorResource(R.color.colorLightGray);
        tabs.setBackgroundResource(R.color.colorPrimary);
        tabs.setFitsSystemWindows(true);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Line line = lines.get(position);
                tabs.setIndicatorColor(line.getNormalColor());
                MapFragment fragment = (MapFragment) homePagerAdapter.instantiateItem(pager, position);
                if (fragment != null) {
                    fragment.loadMap();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        addDrawerItems();
    }

    private void startTabsAndFragments() {
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), lines);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(homePagerAdapter);
        tabs.setViewPager(pager);
        tabs.setIndicatorColor(lines.get(0).getNormalColor());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void addDrawerItems() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Início", R.drawable.ic_home);
        map.put("Sobre", R.drawable.ic_about);

        drawerAdapter = new DrawerAdapter(MainActivity.this, map);
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

    public void addZoom(float zoom)  {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
        if (page != null) {
            ((MapFragment)page).addZoom(zoom);
        }
    }

    public void refreshMap() {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
        if (page != null) {
            ((MapFragment)page).refreshMap();
        }
    }

    public void toggleLocation() {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
        if (page != null) {
            if(!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
            ((MapFragment)page).toggleLocation(new LatLng(currentLatitude, currentLongitude));
        }
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

        MenuItem item = menu.findItem(R.id.action_share_social);
        shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        MenuItemCompat.setActionProvider(item, shareActionProvider);
        
        String shareText = "https://www.google.pt";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(shareText).getIntent();
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    public void toggleLoad(boolean b) {
        pbLoad.setVisibility((b == true) ? View.VISIBLE : View.INVISIBLE);
    }

}
