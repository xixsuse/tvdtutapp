package com.example.root.googlemaps.Class;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import com.example.root.googlemaps.Stop;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GMapV2DirectionAsyncTask extends AsyncTask<String, Document, Void> {

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    private final static String TAG = GMapV2DirectionAsyncTask.class.getSimpleName();
    private Handler handler;
    private ArrayList<Stop> waypoints;

    public GMapV2DirectionAsyncTask(Handler handler, ArrayList<Stop>waypoints) {
        this.handler = handler;
        this.waypoints = waypoints;
    }


    @Override
    protected Void doInBackground(String... params) {
        int i = 0;
        URL url;
        DocumentBuilder builder;
        Document doc;
        HttpURLConnection conn;

        for(Stop way : waypoints) {
            if(i < (waypoints.size()-1)) {
                Stop next = waypoints.get(i+1);
                try {
                    url = new URL("http://maps.googleapis.com/maps/api/directions/xml?"
                            + "origin=" + way.getX() + "," + way.getY()
                            +"&destination=" + next.getX() + "," + next.getY()
                            + "&sensor=false&units=metric&mode=driving");

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    InputStream in = conn.getInputStream();
                    builder = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder();
                    doc = builder.parse(in);
                    publishProgress(doc);
                    conn.disconnect();
                }
                catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return null;
    }


    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Document... result) {
        if (result != null) {

            Document doc = result[0];
            GMapV2Direction md = new GMapV2Direction();
            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions();

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            Message msg = new Message();
            msg.obj = rectLine;
            handler.sendMessage(msg);
        } else {
            Log.d(TAG, "---- GMapV2DirectionAsyncTask ERROR ----");
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}