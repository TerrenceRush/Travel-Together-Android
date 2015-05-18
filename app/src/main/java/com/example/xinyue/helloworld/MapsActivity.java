package com.example.xinyue.helloworld;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(-37.813, 144.962)).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng position = marker.getPosition();
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 11.0f);
        mMap.animateCamera(cameraPosition);

    }




}
