package com.example.xinyue.helloworld.Activities;

import android.content.Intent;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xinyue.helloworld.Network.NetworkOperation;
import com.example.xinyue.helloworld.R;
import com.example.xinyue.helloworld.Fragments.recomFragment;

import com.example.xinyue.helloworld.util.PlanItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends ActionBarActivity{

    public static final String userToken = "CAAMzoVZAzOQEBAKLMTDiYMpZAcRhHZAatreLHcaXzHf59nqLlZBZAMJ5YvOWMFKDwtcO" +
            "Ot5CRhfd3UPychMsZAZCsOxMjO3SlnlPh7jT9NmBLfuzZBT1WmLOXtOj1vnpIJt6roRarYJU9SbZAcZCvc6pZCCWfGeckdy" +
            "DfG7LXfG5yCTTZCPcgkFkJeEegbbRDuDbDWxw0fgQWKQNWRzxJCCXnfkFlWV7KS53zzgZD";

//    public static final String MY_PREFS_NAME = "tokenInfo";
//    String userToken = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("fbAccessToken", "");

    // add get plan information from Xinyue


    // to store plan information
    ArrayList<String> joinlist;
    HashMap<String, String> gmap = new HashMap<String, String>();
    JSONObject detail;
    String planid = "3";


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PlanItem currentItem;
    CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*
            facebook share set up
         */
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallBack);

        /*
            get the current plan item
         */
        currentItem = (PlanItem)getIntent().getSerializableExtra("planItem");

        // get detail data from backend
        getDetail();
//        try {
//            getData(detail);
//        } catch (Exception e) {
//            Log.e("detail plan", "error in parsing detail plan");
//        }

        String title = "find friends to SF";
        String depart_time = "July-01-2015";
        String length = "2";
        String size = "3";
        String participants = "Jessica";
        String holder = "David";
        String describtion = "Let's blow the roof off";

//        String planid = currentItem.get;
//        String title = currentItem.getTitle();
//        String depart_time = currentItem.getDateFrom();
//        int length = currentItem.getDuration();
//        int size = currentItem.getGroupSize();
//        String participants = "";
//        for (int i = 0; i < joinlist.size(); i++) {
//            participants = participants + joinlist.get(i) + ",";
//        }
//        String holder = currentItem.getName();
//        String describtion = currentItem.getDescription();


//




        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("on " + depart_time + " for " + length +" days");

        TextView sizeHolderView = (TextView) findViewById(R.id.size_holder);
        sizeHolderView.setText("Group Size : " + size + "              Created by " + holder);

        TextView participantView = (TextView) findViewById(R.id.participants);
        participantView.setText(participants + " has joined");

        setUpMapIfNeeded();

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText("Why this place is fun : " + describtion);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new recomFragment())
                .commit();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

//    public void getData(JSONObject detail) throws JSONException{
//        JSONObject data = detail.getJSONObject("data");
//        String joinable = data.getString("joinable");
//        String editable = data.getString("editable");
//        String joined = data.getString("joined");
//        JSONArray joined_list = data.getJSONArray("joined_list");
//        if (joined_list != null) {
//            for (int i = 0; i < joined_list.length(); i++) {
//                joinlist.add(joined_list.get(i).toString());
//            }
//        }
//        gmap.put("joinable",joinable);
//        gmap.put("editable",editable);
//        gmap.put("joined", joined);
//    }

    public void getDetail() {
        // get plan information from backend
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkOperation no = new NetworkOperation();
                detail = no.getPlanList(userToken, planid);
                Log.v("detail response", detail.toString());
                String joinable = null;
                String editable = null;
                String joined = null;
                JSONArray joined_list = null;
                try {
                    JSONObject data = detail.getJSONObject("data");
                    joinable = data.getString("joinable");
                    Log.v("joinable", joinable);
                    editable = data.getString("editable");
                    Log.v("editable", editable);
                    joined = data.getString("joined");
                    Log.v("joined", joined);
                    joined_list = data.getJSONArray("joined_list");
                    Log.v("joined_list", joined_list.toString());
                } catch (Exception e) {
                    Log.e("detail", "error in parsing detail");
                }

                if (joined_list != null) {
                    for (int i = 0; i < joined_list.length(); i++) {
                        try {
                            joinlist.add(joined_list.get(i).toString());
                        } catch (Exception e) {
                            Log.e("joinlist", "error in join list");
                        }
                    }
                }
                gmap.put("joinable",joinable);
                gmap.put("editable",editable);
                gmap.put("joined", joined);
            }
        }).start();
    }

    public void loadPlans() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String planid = "8";
                NetworkOperation no = new NetworkOperation();
                JSONObject delete = no.getPlanList(userToken, "all");
                Log.v("plan", delete.toString());
            }
        }).start();
    }


    public void deletePlan () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String planid = "8";
                NetworkOperation no = new NetworkOperation();
                JSONObject delete = no.planActions("delete", userToken, planid);
                Log.v("plan", delete.toString());
            }
        }).start();
    }

    public void joinPlan(){
        String planid = "8";
        NetworkOperation no = new NetworkOperation();
        JSONObject join = no.planActions("join",userToken, planid);
        Log.v("plan", join.toString());
    }

    public void unjoinPlan() {
        String planid = "8";
        NetworkOperation no = new NetworkOperation();
        JSONObject join = no.planActions("plan/unjoin",userToken, planid);
        Log.v("plan", join.toString());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (Boolean.parseBoolean(gmap.get("editable"))) {
            menu.add(menu.NONE, 1, menu.NONE, "Edit Plan");
            menu.add(menu.NONE, 2, menu.NONE, "Delete Plan");
        }
        else if (Boolean.parseBoolean(gmap.get("joinable")) && !Boolean.parseBoolean(gmap.get("joined"))) {
            menu.add(menu.NONE, 3, menu.NONE, "Join Plan");
        } else if (Boolean.parseBoolean(gmap.get("joined"))) {
            menu.add(menu.NONE, 4, menu.NONE, "Disjoin Plan");
        }

//          if (true) {
//              menu.add(menu.NONE, 1, menu.NONE, "Edit Plan");
//              menu.add(menu.NONE, 2, menu.NONE, "Delete Plan");
//          }
//          else {
//              menu.add(menu.NONE, 3, menu.NONE, "Join Plan");
//              menu.add(menu.NONE, 4, menu.NONE, "Disjoin Plan");
//          }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case 1:
                loadPlans(); break;
            case 2:
                deletePlan();

                break;
            case 3:
                joinPlan();  break;
            case 4:
                unjoinPlan();  break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            SupportMapFragment mMapFragment = (SupportMapFragment) (getSupportFragmentManager()
                    .findFragmentById(R.id.map));
            ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
            params.height = 300;
            mMapFragment.getView().setLayoutParams(params);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String city_name = "new york";
        LatLng ll = null;
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(city_name, 5);
                for (Address add : addresses) {
                    if (add.hasLatitude() && add.hasLongitude()) {
                        ll = new LatLng(add.getLatitude(), add.getLongitude());
                        break;
                    }
                }
            }
            catch (IOException e) {

            }
        }
        Marker marker = mMap.addMarker(new MarkerOptions().position(ll).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng position = marker.getPosition();
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 11.0f);
        mMap.animateCamera(cameraPosition);


    }


    /*
        Methods for facebook share
     */
    public FacebookCallback<Sharer.Result> shareCallBack = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            //showToast(message(R.string.title_fbShare)).show();
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException error) {
            //showToast(message(R.string.msgerr_shareOnFB) + " -- " + error.getMessage()).show();
        }
    };

    public void shareToFacebook(View v){
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent adShareContent = new ShareLinkContent.Builder()
                    .setContentTitle(currentItem.getTitle())
                    .setContentDescription(currentItem.getDescription())
                    .build();

            shareDialog.show(adShareContent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }






}
