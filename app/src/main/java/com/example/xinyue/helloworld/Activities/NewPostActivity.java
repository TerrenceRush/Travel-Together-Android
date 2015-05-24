package com.example.xinyue.helloworld.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.example.xinyue.helloworld.Activities.ListActivity;
import com.example.xinyue.helloworld.Network.NetworkOperation;
import com.example.xinyue.helloworld.R;
import com.example.xinyue.helloworld.util.PlanGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewPostActivity extends ActionBarActivity {
    public static final String MY_PREFS_NAME = "tokenInfo";

    private EditText groupSize;
    private EditText departureDate;
    private EditText returnDate;
    private EditText addFriends;
    private ArrayList<String> friendIdList = new ArrayList<String>();
    private ArrayList<String> friendNameList = new ArrayList<String>();
    private Date deptDate = null;
    private Date retDate = null;
    private boolean isFriendIn[];
    private boolean tmpFriendIn[];
    private String token;
    private String query;
    private JSONObject res;
    private RadioGroup radioGroup;
    private RadioButton defaultButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        context = this;

        if (getIntent().getExtras() != null) {
            for(String a : getIntent().getExtras().getStringArrayList("friendIdList")) {
                friendIdList.add(a);
            }
            for(String a : getIntent().getExtras().getStringArrayList("friendNameList")) {
                friendNameList.add(a);
            }

        }
        isFriendIn = new boolean[friendNameList.size()];
        tmpFriendIn = new boolean[friendNameList.size()];
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        defaultButton = (RadioButton) findViewById(R.id.radio_option1);
        radioGroup.check(defaultButton.getId());

        /*
            Set up action bar
         */
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.newpost_actionbar, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(actionBarView);

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
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                deptDate = tmp.getTime();
                departureDate.setText(df.format(tmp.getTime()));
                departureDate.clearFocus();
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
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar tmp = Calendar.getInstance();
                tmp.set(year, month, day);
                retDate = tmp.getTime();
                returnDate.setText(df.format(tmp.getTime()));
                returnDate.clearFocus();
            }
        }, mYear, mMonth, mDay);
        dpg.show();
    }

    public void showFriendList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Friends List");
        String[] nameList = new String[friendNameList.size()];
        Log.i("friends number", "2");

//        builder.setMultiChoiceItems(nameList, isFriendIn, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
//                if (isChecked && (which < 2))
//                    tmpFriendIn[which] = true;
//                else if (which < 2)
//                    tmpFriendIn[which] = false;
//            }
//        });

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
                addFriends.clearFocus();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

            }
        });
        builder.show();
    }


    public void moveToList(View view){
        onBackPressed();
    }


    public void sendMessage(View view){

        final EditText titleField = (EditText)findViewById(R.id.title);
        String title = titleField.getText().toString();

        final EditText destinationField = (EditText) findViewById(R.id.destination);
        String destination = destinationField.getText().toString();
        final String TAG = "MyActivity";

        final EditText departureDateField = (EditText) findViewById(R.id.departure_date);
        String departureDate = departureDateField.getText().toString();

        final EditText sizeField = (EditText) findViewById(R.id.group_size);
        String size = sizeField.getText().toString();
        if(size.equals(""))
            size = "2";


        final EditText informationField = (EditText) findViewById(R.id.addtional_information);
        String information = informationField.getText().toString();

//        Intent intent = new Intent(context, Welcome.class);
//        startActivity(intent);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        int selected = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)findViewById(selected);
        int type = 1;
        if (radioButton.getText().toString().equalsIgnoreCase("Friends"))
            type = 2;
        if(radioButton.getText().toString().equalsIgnoreCase("Private"))
            type = 3;


        ArrayList<String> addedFriendsId = new ArrayList<String>();
        for (int i= 0; i<isFriendIn.length; i++){
            if(isFriendIn[i] == true)
                addedFriendsId.add(friendIdList.get(i));
        }

        if(title.equals("") || destination.equals("") || deptDate == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please complete the form");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
                });
            builder.show();
        }
        else{
            int days = 0;
            if(retDate != null){
                days = (int)(retDate.getTime() - deptDate.getTime())/(24 * 60 * 60 * 1000);
            }



            String text = PlanGenerator.getPlanString(title, destination, departureDate, Integer.toString(days), information, Integer.toString(type), size, addedFriendsId);

            //String text = "aa";
            //String text = PlanGenerator.getPlanString(title, destination, departureDate, days, information, type, Integer.parseInt(size), addedFriendsId);

            token = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).getString("fbAccessToken", "");
//        Log.i("before_add", destination + " " + departureDate + " duration: " + Integer.toString(days) + " info: " + information + " type: " + Integer.toString(type) + " size: " + size);
            Log.i("query", text);
        query = text;
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkOperation no = new NetworkOperation();
                res = no.addPlan(token, query);
                String msg = null;
                try {
                    msg = res.getJSONObject("err").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(msg != null && msg.equalsIgnoreCase("create success")){
                    Log.i("flag", "success");
                    //Toast.makeText(context, "Create Successfully!", Toast.LENGTH_LONG).show();
                }
                findViewById(R.id.newpost_addfriend).setVisibility(View.GONE);
                mUIHandler.sendEmptyMessage(0);


            }
        }).start();



        }





        /**
         * proceed to post the travel use above information
         */
    }


    public void onRadioButtonClicked(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedID = rg.getCheckedRadioButtonId();

        if(selectedID == R.id.radio_option3){
            findViewById(R.id.newpost_addfriend).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.newpost_addfriend).setVisibility(View.GONE);
        }

        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
        String privacy = selectedRadioButton.getText().toString();
        String test = "test";
    }

    @SuppressLint("HandlerLeak")
    private Handler mUIHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {


        Toast.makeText(context, "Create Succesfully!", Toast.LENGTH_LONG).show();
            onBackPressed();



        }

    };



}
