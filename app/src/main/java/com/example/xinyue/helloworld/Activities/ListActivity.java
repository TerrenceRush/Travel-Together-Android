package com.example.xinyue.helloworld.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.xinyue.helloworld.DrawerItemCustomAdapter;
import com.example.xinyue.helloworld.Network.NetworkOperation;
import com.example.xinyue.helloworld.ObjectDrawerItem;
import com.example.xinyue.helloworld.R;
import com.example.xinyue.helloworld.customWidget.PullDownView;
import com.example.xinyue.helloworld.util.PlanAdapter;
import com.example.xinyue.helloworld.util.PlanItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListActivity extends ActionBarActivity {
    private static AccessToken accessToken;
    private Spinner naviSpinner;
    public static final String MY_PREFS_NAME = "tokenInfo";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mNavigationDrawerItemTitles;


    public static class contentFragment extends Fragment implements
            PullDownView.OnPullDownListener {
        private ListView mListView;
        private PullDownView mPullDownView;
        private List<PlanItem> listItems;
        private ProgressDialog mProgressDialog = null;
        private PlanAdapter adapter;
        private String type = "all";

        public contentFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.list_content, container, false);
            mPullDownView = (PullDownView) rootView.findViewById(R.id.pulldown_listview);
            mProgressDialog = new ProgressDialog(this.getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Please Wait...");
            listItems = new ArrayList<PlanItem>();
            mPullDownView.setOnPullDownListener(this);
            mListView = mPullDownView.getListView();
            mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            adapter = new PlanAdapter(this.getActivity(), R.layout.plan_item,
                    listItems);
            mListView.setAdapter(adapter);
            mPullDownView.setShowHeader();



            final List<String> friendList = new ArrayList<String>();
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, friendList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //request for facebook friend list
            GraphRequest request = GraphRequest.newMyFriendsRequest(ListActivity.accessToken, new GraphRequest.GraphJSONArrayCallback() {
                @Override
                public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {

//                mainTextView.setText(jsonArray.optString("gender"));
//                Log.i("Facebook", friends.toString());
                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            friendList.add(((JSONObject) jsonArray.get(i)).optString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //friendListSpinner.setAdapter(dataAdapter);


                }
            });

            request.executeAsync();
            loadPlans();
            return rootView;

        }

        public void loadPlans(){
            final Context context = this.getActivity();
            if (this.getActivity() != null && !((Activity) this.getActivity()).isFinishing())
                mProgressDialog.show();

            new Thread(new Runnable(){
                public void run(){
//                    NetworkOperation networkOperation = new NetworkOperation();
//                    JSONObject response = networkOperation.getPlanList(accessToken.getToken(), type);
//                    if(response == null){
//                        //Toast.makeText(context, "Empty list", Toast.LENGTH_LONG);
//                    }
//                    else{
//                        try {
//                            JSONObject data = response.getJSONObject("data");
//                            JSONArray objs = data.getJSONArray("planlist");
//                            listItems.clear();
//                            for(int i=0; i<objs.length();i++){
//                                JSONObject tmp = (JSONObject) objs.get(i);
//                                PlanItem tmpItem =  new PlanItem();
//                                tmpItem.setTitle(tmp.getString("title"));
//                                //tmpItem.setCurrentSize(tmp.getInt("length"));
//                                tmpItem.setGroupSize(tmp.getInt("limit"));
//                                tmpItem.setName(((JSONObject)tmp.getJSONObject("holder")).getString("name"));
//                                tmpItem.setDateFrom(new Date(tmp.getString("depart_time")));
//                                //tmpItem.setDuration(tmp.getInt("")));
//                                tmpItem.setDescription(tmp.getString("description"));
//                                tmpItem.setDestination(tmp.getString("destination"));
//                                listItems.add(tmpItem);
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    PlanItem faker1 = new PlanItem();
                    faker1.setDestination("Seattle, WA");
                    faker1.setDescription("Good trip");
                    faker1.setName("Xinyue Li");
                    faker1.setGroupSize(5);
                    faker1.setTitle("HIIII");
                    faker1.setDateFrom("2015-08-19");
                    faker1.setAvatar("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/v/t1.0-1/c20.0.80.80/p80x80/10463029_563489853770182_4529375127587693870_n.jpg?oh=5e4a3d5637bbe81d220f0dbbb6e5be7a&oe=5603E0AE&__gda__=1439410567_d2a73534f9278927b605d78107eab026");
                    listItems.add(faker1);
                    adapter.notifyDataSetChanged();

                }

            }).start();
        }



        @Override
        public void onRefresh() {

            loadPlans();
        }

        @Override
        public void onMore() {

            loadPlans();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.filter_drawer);
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_copy, mNavigationDrawerItemTitles[0]);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_refresh, mNavigationDrawerItemTitles[1]);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_share, mNavigationDrawerItemTitles[2]);
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_action_share, mNavigationDrawerItemTitles[3]);
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_action_share, mNavigationDrawerItemTitles[4]);

        DrawerItemCustomAdapter drawerItemCustomAdapter = new DrawerItemCustomAdapter(this, R.layout.drawer_item, drawerItem);
        mDrawerList.setAdapter(drawerItemCustomAdapter);





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


        //store the access token of facebook account to sharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Gson gsonAccessToken = new Gson();
        String jsonAccessToken = gsonAccessToken.toJson(accessToken);
        editor.putString("fbAccessToken", accessToken.getToken());
        editor.commit();

        //String token = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("fbAccessToken", "");

        NetworkOperation networkOperation = new NetworkOperation();
        //JSONObject getPlanListResponse = networkOperation.getPlanList(accessToken.getToken(), "all");
        Log.i("a","a");

        //get access token of facebook account from sharedPreferences
        /*
        String json = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(MY_PREFS_NAME, "");
        AccessToken accessToken1 = gsonAccessToken.fromJson(json, AccessToken.class);
        */


        //request to backend to get the specific userId  ----- old version --------
        /*RequestQueue queue = Volley.newRequestQueue(this);
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
        */

        //set the content view of frame layout
        Fragment fragment = new contentFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();


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
