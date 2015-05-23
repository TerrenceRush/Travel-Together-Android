package com.example.xinyue.helloworld.Activities;

import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ListActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    private static AccessToken accessToken;
    private Spinner naviSpinner;
    public static final String MY_PREFS_NAME = "tokenInfo";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mNavigationDrawerItemTitles;
    private static ArrayList<String> friendIdList = new ArrayList<String>();
    private static ArrayList<String> friendNameList = new ArrayList<String>();
    private static HashSet<String> friendSet = new HashSet<String>();


    public static class contentFragment extends Fragment implements
            PullDownView.OnPullDownListener {
        private ListView mListView;
        private PullDownView mPullDownView;
        private List<PlanItem> listItems;
        private ProgressDialog mProgressDialog = null;
        private PlanAdapter adapter;
        private String type = "all";
        private Context context;

        public contentFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.list_content, container, false);
            context = this.getActivity();
            mPullDownView = (PullDownView) rootView.findViewById(R.id.pulldown_listview);
            mProgressDialog = new ProgressDialog(this.getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Please Wait...");
            listItems = new ArrayList<PlanItem>();
            mPullDownView.setOnPullDownListener(this);
            mListView = mPullDownView.getListView();
            mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            mListView.setVerticalScrollBarEnabled(false);
            adapter = new PlanAdapter(this.getActivity(), R.layout.plan_item,
                    listItems);
            mListView.setAdapter(adapter);
            mPullDownView.setShowHeader();
            Bundle listTypeArgs = getArguments();
            type = listTypeArgs.getString("type");

            //set plan item click listener
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context, listItems.get(position-1).getName(), Toast.LENGTH_LONG).show();
                    Intent openDetailActivityIntent = new Intent(context, MapsActivity.class);
                    openDetailActivityIntent.putExtra("planItem", listItems.get(position));
                    startActivity(openDetailActivityIntent);
                }
            });


            loadPlans(false);
            return rootView;

        }

        public void loadPlans(final boolean ifRefresh){
            if (this.getActivity() != null && !(this.getActivity()).isFinishing())
                mProgressDialog.show();

            new Thread(new Runnable(){
                public void run(){
                    NetworkOperation networkOperation = new NetworkOperation();
                    JSONObject response;
                    if(type.equals("friend")){
                        response = networkOperation.getPlanList(accessToken.getToken(), "all");
                    }
                    else {
                        response = networkOperation.getPlanList(accessToken.getToken(), type);

                    }
                    if(response == null){
                        //Toast.makeText(context, "Empty list", Toast.LENGTH_LONG);
                    }
                    else{
                        try {
                            JSONArray objs = response.getJSONArray("data");
                            listItems.clear();
                            for(int i=0; i<objs.length();i++){
                                JSONObject tmp = (JSONObject) objs.get(i);
                                PlanItem tmpItem =  new PlanItem();
                                tmpItem.setTitle(tmp.getString("title"));
                                tmpItem.setCurrentSize(tmp.getInt("count"));
                                tmpItem.setGroupSize(tmp.getInt("limit"));
                                tmpItem.setName((tmp.getJSONObject("holder")).getString("name"));
                                tmpItem.setHolderId(tmp.getJSONObject("holder").getString("id"));
                                tmpItem.setDateFrom(tmp.getString("depart_time"));
                                tmpItem.setDuration(tmp.getInt("length"));
                                tmpItem.setDescription(tmp.getString("description"));
                                tmpItem.setDestination(tmp.getString("destination"));
                                //tmpItem.setAvatar(tmp.getJSONObject("holder").getString("avatar"));
                                tmpItem.setAvatar("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/v/t1.0-1/c20.0.80.80/p80x80/10463029_563489853770182_4529375127587693870_n.jpg?oh=5e4a3d5637bbe81d220f0dbbb6e5be7a&oe=5603E0AE&__gda__=1439410567_d2a73534f9278927b605d78107eab026");
                                listItems.add(tmpItem);
                            }
                            if(type.equals("friend")){
                                for(int i = 0;i<listItems.size();i++){

                                    PlanItem tmp = listItems.get(i);

                                    if(!friendSet.contains(tmp.getHolderId()))
                                        listItems.remove(i);

                                }
                            }
                            mUIHandler.sendEmptyMessage(0);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (ifRefresh)
                            mPullDownView.RefreshComplete();
                        mPullDownView.notifyDidMore();
                    }





                }

            }).start();
        }



        @Override
        public void onRefresh() {

            loadPlans(true);
        }

        @Override
        public void onMore() {

            loadPlans(false);
        }

        @SuppressLint("HandlerLeak")
        private Handler mUIHandler = new Handler() {

            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                //do remember to dismiss dialog when exiting an activity.
                if (context != null && !((Activity) context).isFinishing())
                    mProgressDialog.cancel();

                //Only the original thread that created a view hierarchy can touch its views. So I have to put it here not in the thread of network operation.
                adapter.notifyDataSetChanged();



            }

        };



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
        optionsList.add("All Plans");
        optionsList.add("My Plans");
        optionsList.add("Friends' Plans");
        optionsList.add("Joined Plans");
        ArrayAdapter<String> dataAdapterForNavi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsList);
        dataAdapterForNavi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        naviSpinner.setAdapter(dataAdapterForNavi);
        naviSpinner.setOnItemSelectedListener(this);


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

        //            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, friendList);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //request for facebook friend list
        GraphRequest request = GraphRequest.newMyFriendsRequest(ListActivity.accessToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {

//                mainTextView.setText(jsonArray.optString("gender"));
//                Log.i("Facebook", friends.toString());
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        friendIdList.add(((JSONObject) jsonArray.get(i)).optString("id"));
                        friendNameList.add(((JSONObject) jsonArray.get(i)).optString("name"));
                        friendSet.add(((JSONObject) jsonArray.get(i)).optString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
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
        if (id == R.id.filter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void NewPostLaunch(View v){
        Intent openNewPostActivityIntent = new Intent(this, NewPostActivity.class);

        /*
        send friend list to new post activity
         */
        openNewPostActivityIntent.putStringArrayListExtra("friendIdList", friendIdList);
        openNewPostActivityIntent.putStringArrayListExtra("friendNameList", friendNameList);
        startActivity(openNewPostActivityIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        //set the content view of frame layout
        Fragment fragment = new contentFragment();
        Bundle listTypeArgs = new Bundle();

        switch(pos){
            case 0:{
                listTypeArgs.putString("type", "all");
                break;
            }
            case 1:{
                listTypeArgs.putString("type", "mine");
                break;
            }
            case 2:{
                listTypeArgs.putString("type", "friend");
                break;
            }
            case 3:{
                listTypeArgs.putString("type", "joined");
                break;
            }
            default:{
                listTypeArgs.putString("type", "all");
                break;
            }

        }
        fragment.setArguments(listTypeArgs);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}
