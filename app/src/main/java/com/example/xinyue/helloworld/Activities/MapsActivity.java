package com.example.xinyue.helloworld.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import imageCache.AsyncImageLoader;

public class MapsActivity extends ActionBarActivity{

//    public static final String userToken = "CAAMzoVZAzOQEBABlhZCumJV7oNY3kSnQZCOqLzVaYkyB3vtWBKadflJlTblcD1fTsVRyVkEqSc" +
//            "ij13MbU0MobDm4wjnT6oE8J9odda3qPWZCgAivjJMUpkDk6XuSWGeCAym7aLvZC24rwxSrbd7x9VH9wXh0J" +
//            "eCZAoHFThhLZBxjl2KbSGOXnziQUTUt2K2PX524lPzRK02Yi4WkxaBA7dSKZAZACDvG050AZD";
public static final String MY_PREFS_NAME = "tokenInfo";
    public String userToken;
    private Context context;
    private String participants;


    private Menu mOptionsMenu;

    // add get plan information from Xinyue


    // to store plan information
    ArrayList<String> joinlist = new ArrayList<String>();
    HashMap<String, String> gmap = new HashMap<String, String>();
    JSONObject detail;
    String planid = "40";


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PlanItem currentItem;
    CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private ArrayList<String> city_name = new ArrayList<String>(Arrays.asList("las vegas"));


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;



        userToken = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("fbAccessToken", "");


        //set up the view of action bar
        LayoutInflater inflator = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.map_actionbar, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(actionBarView);

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

//        String title = "Find Friends to SF";
//        String depart_time = "July-01-2015";
//        String length = "2";
//        String size = "3";
//        String participants = "Jessica";
//        String holder = "David";
//        String description = "Let's blow the roof off";

        String return_time = "July-28-2015";
        planid = currentItem.getId();
        String title = currentItem.getTitle();
        String depart_temp = currentItem.getDateFrom();
        String[] depart = depart_temp.split(" ");
        String depart_time = depart[0];
        String length = Integer.toString(currentItem.getDuration());
        String size = Integer.toString(currentItem.getGroupSize());
        String holder = currentItem.getName();
        String description = currentItem.getDescription();
        city_name.set(0, currentItem.getDestination());


        gmap.put("title", title);
        gmap.put("depart_time", depart_time);
        gmap.put("length", length);
        gmap.put("size", size);
        gmap.put("holder", holder);
        gmap.put("description", description);
        gmap.put("destination", city_name.get(0));
        gmap.put("return_time", return_time);




        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);

        TextView time = (TextView) findViewById(R.id.time);
        time.setText(Html.fromHtml("on <b><font color=\"blue\">" + depart_time + "</font></b> for <b><font color=\"blue\">" + length+ "</font></b> days"));

        TextView sizeHolderView = (TextView) findViewById(R.id.size_holder);
        sizeHolderView.setText(Html.fromHtml("Group Size : <b><font color=\"red\">" + size + "</font></b>") + "                   Created by " + holder);


        setUpMapIfNeeded(city_name.get(0));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(Html.fromHtml("<b><font color=\"blue\"> Why this place is fun :</font></b>" + description));

        TextView recommendation = (TextView) findViewById(R.id.fourRecom);
        recommendation.setText(Html.fromHtml("<b><font color=\"blue\">Recommendation from Foursquare</font></b>"));
        LatLng lc = null;
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(city_name.get(0), 5);
                for (Address add : addresses) {
                    if (add.hasLatitude() && add.hasLongitude()) {
                        lc = new LatLng(add.getLatitude(), add.getLongitude());
                        break;
                    }
                }
            }
            catch (IOException e) {

            }
        }
        String location = "";
        location = location+lc.latitude +"," + lc.longitude;
        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        recomFragment recomf = new recomFragment();
        recomf.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, recomf)
                .commit();

        //ImageView avatar = (ImageView) findViewById(R.id.detail_avatar);


//        try {
//            avatar.setImageBitmap(getImage(currentItem.getAvatar()));
//        } catch (Exception e) {
//            Log.v("image error", "image loading error");
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(city_name.get(0));
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

                            JSONObject friend = joined_list.getJSONObject(i);
                            String name = friend.getString("name");
                            joinlist.add(name);
                            Log.v("name", name);
                        } catch (Exception e) {
                            Log.e("joinlist", "error in join list");
                        }
                    }
                }
                participants = "";
                if (joinlist != null && joinlist.size() != 0) {
                    for (int i = 0; i < joinlist.size(); i++) {
                        participants = participants + joinlist.get(i) + " ";
                    }
                } else {
                    participants = "no one ";
                }
                TextView participantView = (TextView) findViewById(R.id.participants);
                participantView.setText(participants + " has joined");
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
                NetworkOperation no = new NetworkOperation();
                JSONObject delete = no.planActions("delete", userToken, planid);
                mUIHandler.sendEmptyMessage(2);
                Log.v("plan", delete.toString());
            }
        }).start();
    }

    public void joinPlan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkOperation no = new NetworkOperation();
                JSONObject join = no.planActions("join",userToken, planid);
                String myname = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("name", "");
                joinlist.add(myname);
                participants = "";
                if (joinlist != null && joinlist.size() != 0) {
                    for (int i = 0; i < joinlist.size(); i++) {
                        participants = participants + joinlist.get(i) + " ";
                    }
                } else {
                    participants = "no one ";
                }
                gmap.put("joinable", "false");
                gmap.put("joined", "true");
                mUIHandler.sendEmptyMessage(0);
                Log.v("plan", join.toString());
            }
        }).start();
    }

    public void unjoinPlan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkOperation no = new NetworkOperation();
                JSONObject join = no.planActions("plan/unjoin",userToken, planid);
                String myname = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("name", "");
                joinlist.remove(myname);
                participants = "";
                if (joinlist != null && joinlist.size()!= 0) {
                    for (int i = 0; i < joinlist.size(); i++) {
                        participants = participants + joinlist.get(i) + " ";
                    }
                } else {
                    participants = "no one ";
                }
                gmap.put("joinable", "true");
                gmap.put("joined", "false");
                mUIHandler.sendEmptyMessage(1);
                Log.v("plan", join.toString());
                Log.v("plan", join.toString());
            }
        }).start();
    }

    public void moveToEditPost() {
        Intent intent = new Intent(this, EditPost.class);
        intent.putExtra("datamap", gmap);
        intent.putExtra("joinlist", joinlist);
        startActivity(intent);
    }

    public void backToList(View view) {
        onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        mOptionsMenu = menu;
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
                moveToEditPost(); break;
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
     * call {@link #setUpMap(String city_name)} once when {@link #mMap} is not null.
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
    private void setUpMapIfNeeded(String city_name) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            SupportMapFragment mMapFragment = (SupportMapFragment) (getSupportFragmentManager()
                    .findFragmentById(R.id.map));
            ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
            params.height = 400;
            mMapFragment.getView().setLayoutParams(params);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(city_name);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(String city_name) {
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



    public Bitmap getImage(String url) throws IOException{
        URL newurl = new URL(url);
        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        return  mIcon_val;
    }

    @SuppressLint("HandlerLeak")
    private Handler mUIHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :{
                    TextView participantView = (TextView) findViewById(R.id.participants);
                    participantView.setText(participants + " has joined");
                    onPrepareOptionsMenu(mOptionsMenu);
                    break;
                }
                case 1 :{
                    TextView participantView = (TextView) findViewById(R.id.participants);
                    participantView.setText(participants + " has joined");
                    onPrepareOptionsMenu(mOptionsMenu);
                    break;
                }
                case 2 :{
                    Intent intent = new Intent();
                    intent.putExtra("planId", planid);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
            }






        }

    };


}
