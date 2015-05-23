package com.example.xinyue.helloworld.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xinyue.helloworld.Activities.ListActivity;
import com.example.xinyue.helloworld.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewPostActivity extends Activity {
    private EditText groupSize;
    private EditText departureDate;
    private EditText returnDate;
    private ArrayList<String> friendIdList = new ArrayList<String>();
    private ArrayList<String> friendNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        if (getIntent().getExtras() != null) {
            for(String a : getIntent().getExtras().getStringArrayList("friendIdList")) {
                friendIdList.add(a);
            }
            for(String a : getIntent().getExtras().getStringArrayList("friendNameList")) {
                friendNameList.add(a);
            }

        }
        //addListenerOnButton();
        //addListenerOnButton1();
        addListenerOnGroupSize();
        addListenerOnDepartDate();
        addListenerOnReturnDate();


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

    public void addListenerOnGroupSize(){
        groupSize = (EditText)findViewById(R.id.group_size);
        groupSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showGroupSize();

            }
        });
    }

    public void addListenerOnDepartDate(){
        departureDate = (EditText)findViewById(R.id.departure_date);
        departureDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showDepartDate(view);

            }
        });
    }

    public void addListenerOnReturnDate(){
        returnDate = (EditText)findViewById(R.id.return_date);
        returnDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showReturnDate(view);

            }
        });
    }

    public void showGroupSize()
    {

        final Dialog d = new Dialog(NewPostActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.group_size_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(20); // max value 100
        np.setMinValue(2);   // min value 0
        np.setWrapSelectorWheel(false);
        //np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                groupSize.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

    public void showDepartDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                departureDate.setText(df.format(tmp.getTime()));
            }
        }, mYear, mMonth, mDay);
        dpg.show();
    }

    public void showReturnDate(View view){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                returnDate.setText(df.format(tmp.getTime()));
            }
        }, mYear, mMonth, mDay);
        dpg.show();
    }
//
//    public void addListenerOnButton() {
//
//        final Context context = this;
//
//        Button button = (Button) findViewById(R.id.cancel);
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                Intent intent = new Intent(context, MapsActivity.class);
//                startActivity(intent);
//
//            }
//
//        });
//
//    }

    public void moveToList(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

//    public void addListenerOnButton1() {
//        final Context context = this;
//        Button button = (Button) findViewById(R.id.post);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg1) {
//                // get information from EditText
//                final EditText destinationField = (EditText) findViewById(R.id.destination);
//                String destination = destinationField.getText().toString();
//
//                final String TAG = "MyActivity";
//
//                Log.d(TAG, "index=" + destination);
//
//                String test = "test";
//
//                final EditText departureDateField = (EditText) findViewById(R.id.departure_date);
//                String departureDate = departureDateField.getText().toString();
//
//                final EditText sizeField = (EditText) findViewById(R.id.group_size);
//                String size = sizeField.getText().toString();
//
//                // get information from RadioGroup
//
//                /*RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
//                int selectedID = rg.getCheckedRadioButtonId();
//
//                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
//                String privacy = selectedRadioButton.getText().toString();*/
//
//                // get information from checkbox
//                final CheckBox responseCheck = (CheckBox) findViewById(R.id.shareFacebook);
//                boolean share = responseCheck.isChecked();
//
//                // get information of traveling members
//                final Spinner spinnerMem = (Spinner) findViewById(R.id.spinner);
//                String members = spinnerMem.getSelectedItem().toString();
//
//                // get additional information
//                final EditText informationField = (EditText) findViewById(R.id.addtional_information);
//                String information = informationField.getText().toString();
//
//                System.out.print(destination + departureDate + size);
//
//
//                Intent intent = new Intent(context, Welcome.class);
//                startActivity(intent);
//            }
//        });
//    }

    public void sendMessage(View view){
        final Context context = this;
        final EditText destinationField = (EditText) findViewById(R.id.destination);
        String destination = destinationField.getText().toString();
        final String TAG = "MyActivity";

        Log.d(TAG, "index=" + destination);

        String test = "test";
        final EditText departureDateField = (EditText) findViewById(R.id.departure_date);
        String departureDate = departureDateField.getText().toString();

        final EditText sizeField = (EditText) findViewById(R.id.group_size);
        String size = sizeField.getText().toString();

        final CheckBox responseCheck = (CheckBox) findViewById(R.id.shareFacebook);
        boolean share = responseCheck.isChecked();

        final Spinner spinnerMem = (Spinner) findViewById(R.id.spinner);
        String members = spinnerMem.getSelectedItem().toString();

        final EditText informationField = (EditText) findViewById(R.id.addtional_information);
        String information = informationField.getText().toString();

        Intent intent = new Intent(context, Welcome.class);
        startActivity(intent);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        int selected = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)findViewById(selected);
        String radioText = radioButton.getText().toString();

        /**
         * proceed to post the travel use above information
         */
    }


    public void onRadioButtonClicked(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedID = rg.getCheckedRadioButtonId();

        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
        String privacy = selectedRadioButton.getText().toString();
        String test = "test";
    }




}
