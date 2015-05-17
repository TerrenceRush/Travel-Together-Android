package com.example.xinyue.helloworld;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class recomFragment extends Fragment {

    ArrayAdapter<String> placeRecom;
    public recomFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            GetRecommendation fetchPlace = new GetRecommendation();
            fetchPlace.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = {
                "West place, Chinese, 123456789",
                "Barnard buffet, American, 123456789",
                "Luoyang Uncle, Chinese, 123456789",
                "West place, Chinese, 123456789",
                "Barnard buffet, American, 123456789",
                "Luoyang Uncle, Chinese, 123456789"
        };
        ArrayList<String> recommendation = new ArrayList<String>(Arrays.asList(data));

        placeRecom = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, R.id.item_textview, recommendation);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listview = (ListView) rootView.findViewById(R.id.listview_recom);
        listview.setAdapter(placeRecom);


        return rootView;
    }

    private String parseRecommendation (String result) throws JSONException{
        JSONObject obj = new JSONObject(result);

//            String result = obj.toString();
//            return result;
        Log.d("String size", "size"+result.length());
        JSONObject response = obj.getJSONObject("response");
//        Log.d("response", response.toString());
//        JSONArray groups = response.getJSONArray("groups");
//        Log.d("groups", groups.toString());
//        JSONObject recommendation = groups.getJSONObject(0);
//        Log.d("recommendation", recommendation.toString());
//        JSONArray items = recommendation.getJSONArray("items");
//        JSONObject item = items.getJSONObject(0);
//        Log.d("item", item.toString());
//        JSONObject venue = item.getJSONObject("venue");
//        String name = venue.getString("name");
//        JSONArray categories = venue.getJSONArray("categories");
//        JSONObject category = categories.getJSONObject(0);
//        String type = category.getString("name");
//
//        String contact = venue.getJSONObject("contact").getString("formattedPhone");
//
//        String tips = item.getJSONArray("tips").getJSONObject(0).getString("text");
//        Log.d("test2", "venue :" + name + type + contact + tips);
//        JSONObject venueRec = new JSONObject();
//        venueRec.put("name", name);
//        venueRec.put("type", type);
//        venueRec.put("contact", contact);
//        venueRec.put("tips", tips);
//        String res = venueRec.toString();
//        Log.v("test", res);
        JSONObject venue = response.getJSONObject("venue");
        Log.d("test", venue.toString());
        return venue.toString();
    }

    protected String convertStreamToString(java.io.InputStream is) {
        //@SuppressWarnings("resource")
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public class GetRecommendation extends AsyncTask<Void, Void, Void>  {
        private final String LOG_TAG = GetRecommendation.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String recomStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //the foursquare ID and Foursquare secret
                final String clientID = "M5L2PT2YIELOHSMCLOTOCGHGONUNKYHL3HUPZ5REZ44DLCEB";
                final String clientSecret = "BEJFOTI3FKXLEDSTHEDCVWPSBAUXVM4MD5QAJXQFQWOOU1SF";

                // test lat and lon
                final String lat = "40.7463956";
                final String lon = "-73.9852992";
                String url1 = "https://api.foursquare.com/v2/venues/search?client_id=" +
                        clientID + "&client_secret=" + clientSecret + "&v=20130815&ll=40.7463956,-73.9852992&radius=5";

                // Create the request to Foursqure, and open the connection
                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                //String content = convertStreamToString(inputStream);
                //Log.v(LOG_TAG, "recommended place :" + content.toString());
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                recomStr = buffer.toString();
                Log.d("size", "size" + recomStr.length());
                Log.d("their method", recomStr);
//                String result = null;
//                try {
//                    result = parseRecommendation(content);
//                } catch (Exception e) {
//                    Log.e(LOG_TAG, "ERROR PARSING JSON OBJECT");
//                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}