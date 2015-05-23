package com.example.xinyue.helloworld.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.xinyue.helloworld.util.PlanGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewPostActivity extends Activity {
    public static final String MY_PREFS_NAME = "tokenInfo";

    private EditText groupSize;
    private EditText departureDate;
    private EditText returnDate;
    private EditText addFriends;
    private ArrayList<String> friendIdList = new ArrayList<String>();
    private ArrayList<String> friendNameList = new ArrayList<String>();
    private Date deptDate = null;
    private Date retDate = null;
    private boolean isFriendIn[] = new boolean[friendNameList.size()];
    private boolean tmpFriendIn[] = new boolean[friendNameList.size()];

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
        addListenerOnGroupSize();
        addListenerOnDepartDate();
        addListenerOnReturnDate();
        addListenerOnAddFriend();

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
        groupSize.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showGroupSize();
            }

        });
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
        departureDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDepartDate(v);
            }

        });
        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showDepartDate(view);

            }
        });
    }

    public void addListenerOnReturnDate(){
        returnDate = (EditText)findViewById(R.id.return_date);
        returnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showReturnDate(v);
            }

        });
        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showReturnDate(view);

            }
        });
    }

    public void addListenerOnAddFriend(){
        addFriends = (EditText) findViewById(R.id.add_friend);
        addFriends.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showFriendList();

            }

        });
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showFriendList();

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
                DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                deptDate = tmp.getTime();
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
                DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                retDate = tmp.getTime();
                returnDate.setText(df.format(tmp.getTime()));
            }
        }, mYear, mMonth, mDay);
        dpg.show();
    }

    public void showFriendList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("@string/friend_list");
        String[] nameList = new String[friendNameList.size()];
        builder.setMultiChoiceItems(friendNameList.toArray(nameList), isFriendIn, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked && (which < friendNameList.size()))
                    tmpFriendIn[which] = true;
                else if (which < friendNameList.size())
                    tmpFriendIn[which] = false;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                isFriendIn = Arrays.copyOf(tmpFriendIn, tmpFriendIn.length);
                //Set the name in the text view...
                int count = 0;
                int first = -1;
                for (int i = 0; i < tmpFriendIn.length; i++) {
                    if (tmpFriendIn[i] == true) {
                        count++;
                        if (first == -1)
                            first = i;
                    }
                }
                if (count > 0) {
                    StringBuilder string = new StringBuilder();
                    string.append(friendNameList.get(first));
                    if (count > 1) {
                        string.append(" " + "and " + Integer.toString(count - 1) + " more");
                    }
                    addFriends.setText(string.toString());
                } else {
                    addFriends.setText("Click to Add Friends");
                }
            }
        });
    }


    public void moveToList(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


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
        int type = 1;
        if (radioButton.getText().toString().equalsIgnoreCase("Friends"))
            type = 2;
        if(radioButton.getText().toString().equalsIgnoreCase("2nd degree"))
            type = 3;


        ArrayList<String> addedFriendsId = new ArrayList<>();
        for (int i= 0; i<isFriendIn.length; i++){
            if(isFriendIn[i] == true)
                addedFriendsId.add(friendIdList.get(i));
        }

        int days = 0;
        if(retDate != null){
            days = (int)(retDate.getTime() - deptDate.getTime())/(24 * 60 * 60 * 1000);
        }
        String text = PlanGenerator.getPlanString("none", destination, departureDate, Integer.toString(days), information, Integer.toString(type), size, addedFriendsId);
        String token = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("fbAccessToken", "");
        String query = "http://cloud6998.elasticbeanstalk.com/v1/add/:" + token + "/";
        try {
            query += URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



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
