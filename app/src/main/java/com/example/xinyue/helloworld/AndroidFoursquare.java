package com.example.xinyue.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.client.HttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AndroidFoursquare extends ListActivity {
    ArrayList venuresList;

    //the foursquare ID and Foursquare secret
    final String clientID = "M5L2PT2YIELOHSMCLOTOCGHGONUNKYHL3HUPZ5REZ44DLCEB";
    final String clientSecret = "BEJFOTI3FKXLEDSTHEDCVWPSBAUXVM4MD5QAJXQFQWOOU1SF";

    // test lat and lon
    final String lat = "40.7463956";
    final String lon = "-73.9852992";

    ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "https://api.foursquare.com/v2/venues/search?client_id=" +
                clientID + "&client_secret=" + clientSecret + "&v=20130815&ll=40.7463956,-73.9852992";
        // start the AsyncTask that makes the call for the venus search.

        foursquare fs = new foursquare();
        try {
            fs.getExplore(url);
        } catch (Exception e) {
            Log.e("result", "problem in get result");
        }
    }


    private class foursquare {
        String result;
        protected String getExplore(String url1) throws IOException {
            URL url = new URL(url1);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = (InputStream) urlConnection.getContent();
            String content = convertStreamToString(inputStream);
            JSONObject obj = new JSONObject();
            try {
                obj = new JSONObject(content);
            } catch (Exception e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }

            String result = obj.toString();
            return result;

        }



        protected String convertStreamToString(java.io.InputStream is) {
            //@SuppressWarnings("resource")
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }


}