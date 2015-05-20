package com.example.xinyue.helloworld;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

    private String[] parseRecommendation (String result) throws JSONException{
        JSONObject obj = new JSONObject(result);

//            String result = obj.toString();
//            return result;
        Log.d("String size", "size"+result.length());
        JSONObject response = obj.getJSONObject("response");
        JSONArray venues = response.getJSONArray("venues");
        String[] recom = new String[10];
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            JSONObject venue = venues.getJSONObject(i);
            String name = venue.getString("name");
            String type = venue.getJSONArray("categories").getJSONObject(0).getString("name");
            String distance = venue.getJSONObject("location").getString("distance");
            sb.append(String.format("%-20s", name) + "\n");
            sb.append(String.format("%-30s", "type: "+ type));
            sb.append(String.format("%-30s", "dis: "+ distance));
            recom[i] = sb.toString();
        }
        return recom;
    }


    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
            directory.mkdirs();

            //Now create the file in the above directory and write the contents into it
            File file = new File(directory, sFileName);
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(sBody);
            osw.flush();
            osw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public class GetRecommendation extends AsyncTask<String, Void, String[]>  {
        private final String LOG_TAG = GetRecommendation.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params){
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
                        clientID + "&client_secret=" + clientSecret + "&v=20150519&ll=40.7463956,-73.9852992";

                // Create the request to Foursqure, and open the connection
                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

//		        URLConnection urlConnection = url.openConnection();
//		        InputStream inputStream = (InputStream) urlConnection.getContent();
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
//                Log.d("permission", Environment.getExternalStorageState());
                Log.d("their method", recomStr);

                //generateNoteOnSD("response.txt", recomStr);


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

            try {
                return parseRecommendation(recomStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, "ERROR PARSING JSON OBJECT");
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                placeRecom.clear();
                for(String dayForecastStr : result) {
                    placeRecom.add(dayForecastStr);
                }
                // New data is back from the server.  Hooray!
            }
        }
    }
}