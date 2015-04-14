package com.example.xinyue.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListActivity extends ActionBarActivity {
    private AccessToken accessToken;
    private Spinner friendListSpinner;
    private Spinner naviSpinner;
    private List<String> friendList = new ArrayList<String>();
    public static final String MY_PREFS_NAME = "tokenInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final TextView mainTextView = (TextView) findViewById(R.id.text);
        friendListSpinner = (Spinner) findViewById(R.id.friend_list);

        //set up the view of action bar
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.list_actionbar, null);
        naviSpinner = (Spinner) actionBarView.findViewById(R.id.navigationSpinner);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(actionBarView);
        ArrayList<String> optionsList = new ArrayList<String>();
        optionsList.add("Friends' Plans");
        optionsList.add("My Plan");
        optionsList.add("2nd Degree Plans");
        ArrayAdapter<String> dataAdapterForNavi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsList);
        dataAdapterForNavi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        naviSpinner.setAdapter(dataAdapterForNavi);



        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("accessTokenBundle");
        Log.i("Tag","HERE");

        accessToken = bundle.getParcelable("accessToken");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, friendList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //store the access token of facebook account to sharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Gson gsonAccessToken = new Gson();
        String jsonAccessToken = gsonAccessToken.toJson(accessToken);
        editor.putString("fbAccessToken", jsonAccessToken);
        editor.commit();

        //get access token of facebook account from sharedPreferences
        /*
        String json = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(MY_PREFS_NAME, "");
        AccessToken accessToken1 = gsonAccessToken.fromJson(json, AccessToken.class);
        */


        //request to backend to get the specific userId
        RequestQueue queue = Volley.newRequestQueue(this);
        String getTokenRoute = "/";
        String getTokenRequestUrl = getSharedPreferences("serverInfo", MODE_PRIVATE).getString("serverHost","")+getTokenRoute;
        StringRequest getTokenRequest = new StringRequest(Request.Method.POST, getTokenRequestUrl,
                new Response.Listener<String>(){
                    @Override
                public void onResponse(String response){
                        Log.i("i", response);
                        SharedPreferences.Editor editor2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor2.putString("userToken", response);
                        editor2.commit();
                }
        },
                new Response.ErrorListener(){
                    @Override
                public void onErrorResponse(VolleyError error){
                        Log.i("i", "error");
                        Log.i("i", error.toString());
                    }

                }
        ){
            @Override
        protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("fbAccessToken", accessToken.getToken());
                return params;

            }
        };
        queue.add(getTokenRequest);//add the request to queue, when the network thread pool has available thread, it will be executed


        //request for facebook friend list
        GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
//                Log.i("Tag", jsonArray.toString());

//                mainTextView.setText(jsonArray.optString("gender"));
//                Log.i("Facebook", friends.toString());
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        friendList.add(((JSONObject) jsonArray.get(i)).optString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                friendListSpinner.setAdapter(dataAdapter);


            }
        });

//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "friends");
//        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
