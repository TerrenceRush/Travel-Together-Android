package com.example.xinyue.helloworld;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Edward on 5/15/15.
 */
public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ArrayAdapter<String> placeRecom;
        public PlaceholderFragment() {
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
    }

}
