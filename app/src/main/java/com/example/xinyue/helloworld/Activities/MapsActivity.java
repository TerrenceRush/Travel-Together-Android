package com.example.xinyue.helloworld.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.List;

public class MapsActivity extends ActionBarActivity{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PlanItem currentItem;
    CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        String title = "find friends to SF";
        String depart_time = "July-01-2015";
        String length = "2";
        String size = "3";
        String participants = "Jessica";
        String holder = "David";
        String describtion = "Let's blow the roof off";

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(1,1,1,"test");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.help) {
            return true;
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
            params.height = 400;
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
