package com.example.xinyue.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.xinyue.helloworld.R;

public class NewPostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        addListenerOnButton();
        addListenerOnButton1();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
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

    public void addListenerOnButton() {

        final Context context = this;

        Button button = (Button) findViewById(R.id.cancel);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);

            }

        });

    }

    public void addListenerOnButton1() {
        final Context context = this;
        Button button = (Button) findViewById(R.id.post);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                // get information from EditText
                final EditText destinationField = (EditText) findViewById(R.id.destination);
                String destination = destinationField.getText().toString();

                final String TAG = "MyActivity";

                Log.v(TAG, "index=" + destination);

                String test = "test";

                final EditText departureDateField = (EditText) findViewById(R.id.departure_date);
                String departureDate = departureDateField.getText().toString();

                final EditText sizeField = (EditText) findViewById(R.id.group_size);
                String size = sizeField.getText().toString();

                // get information from RadioGroup

                /*RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
                int selectedID = rg.getCheckedRadioButtonId();

                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
                String privacy = selectedRadioButton.getText().toString();*/

                // get information from checkbox
                final CheckBox responseCheck = (CheckBox) findViewById(R.id.shareFacebook);
                boolean share = responseCheck.isChecked();

                // get information of traveling members
                final Spinner spinnerMem =  (Spinner) findViewById(R.id.spinner);
                String members = spinnerMem.getSelectedItem().toString();

                // get additional information
                final EditText informationField = (EditText) findViewById(R.id.addtional_information);
                String information = informationField.getText().toString();

                System.out.print(destination+departureDate+size);


                Intent intent = new Intent(context, Welcome.class);
                startActivity(intent);
            }
        });
    }


    public void onRadioButtonClicked(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedID = rg.getCheckedRadioButtonId();

        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
        String privacy = selectedRadioButton.getText().toString();
        String test = "test";
    }




}
