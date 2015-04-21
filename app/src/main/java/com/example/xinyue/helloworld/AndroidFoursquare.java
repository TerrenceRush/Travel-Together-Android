package com.example.xinyue.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
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
        protected String getExplore(String url1) throws IOException, JSONException {
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

//            String result = obj.toString();
//            return result;

            JSONObject response = obj.getJSONObject("response");
            JSONArray groups = response.getJSONArray("groups");
            JSONObject recommendation = groups.getJSONObject(0);
            JSONArray items = recommendation.getJSONArray("items");
            JSONObject item = items.getJSONObject(0);

            JSONObject venue = item.getJSONObject("venue");
            String name = venue.getString("name");
            JSONArray categories = venue.getJSONArray("categories");
            JSONObject category = categories.getJSONObject(0);
            String type = category.getString("name");

            String contact = venue.getJSONObject("contact").getString("formattedPhone");

            String tips = item.getJSONArray("tips").getJSONObject(0).getString("text");

            System.out.println(tips);
//		String result = obj.toString();
//		System.out.println(result);

        }



        protected String convertStreamToString(java.io.InputStream is) {
            //@SuppressWarnings("resource")
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }


}