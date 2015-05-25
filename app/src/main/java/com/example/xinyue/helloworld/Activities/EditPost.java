package com.example.xinyue.helloworld.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;

/**
 * Created by Edward on 5/23/15.
 */
public class EditPost extends Activity {
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
    private Context context;
    private String token;
    private String query;
    private JSONObject res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);
        context = this;

//        if (getIntent().getExtras() != null) {
//            for(String a : getIntent().getExtras().getStringArrayList("friendIdList")) {
//                friendIdList.add(a);
//            }
//            for(String a : getIntent().getExtras().getStringArrayList("friendNameList")) {
//                friendNameList.add(a);
//            }
//
//        }
        Intent intent = getIntent();
        HashMap<String, String> planData = (HashMap)intent.getSerializableExtra("datamap");
        Log.v("holder", ":" + planData.get("holder"));

        EditText title = (EditText) findViewById(R.id.edit_title);
        title.setText(planData.get("title"));
        EditText destination = (EditText) findViewById(R.id.edit_destination);
        destination.setText(planData.get("destination"));
        EditText depart_date = (EditText) findViewById(R.id.edit_departure_date);
        depart_date.setText(planData.get("depart_time"));
        EditText return_date = (EditText) findViewById(R.id.edit_return_date);
        return_date.setText(planData.get("return_time"));
//        EditText return_date = (EditText) findViewById(R.id.edit_return_date);
//        return_date.setText(planData.get(""));
        EditText groupSize = (EditText) findViewById(R.id.edit_groupsize);
        groupSize.setText(planData.get("size"));
  //      RadioGroup rg = (RadioGroup) findViewById(R.id.edit_radioGroup);
        EditText description = (EditText) findViewById(R.id.edit_addtional_information);
        description.setText(planData.get("description"));


        addListenerOnGroupSize();
        addListenerOnDepartDate();
        addListenerOnReturnDate();
        addListenerOnAddFriend();



    }

    public void addListenerOnGroupSize(){
        groupSize = (EditText)findViewById(R.id.edit_groupsize);
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


    public void showGroupSize()
    {

        final Dialog d = new Dialog(EditPost.this);
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

    public void addListenerOnAddFriend(){
        addFriends = (EditText) findViewById(R.id.edit_add_friend);
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

    public void addListenerOnDepartDate(){
        departureDate = (EditText)findViewById(R.id.edit_departure_date);
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
        returnDate = (EditText)findViewById(R.id.edit_return_date);
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
        final EditText titleField = (EditText)findViewById(R.id.edit_title);
        String title = titleField.getText().toString();

        final EditText destinationField = (EditText) findViewById(R.id.edit_destination);
        String destination = destinationField.getText().toString();
        final String TAG = "MyActivity";

        final EditText departureDateField = (EditText) findViewById(R.id.edit_departure_date);
        String departureDate = departureDateField.getText().toString();

        final EditText sizeField = (EditText) findViewById(R.id.edit_groupsize);
        String size = sizeField.getText().toString();
        if(size.equals(""))
            size = "2";


        final EditText informationField = (EditText) findViewById(R.id.edit_addtional_information);
        String information = informationField.getText().toString();

//        Intent intent = new Intent(context, Welcome.class);
//        startActivity(intent);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.edit_radioGroup);
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
        else {
            int days = 0;
            if (retDate != null) {
                days = (int) (retDate.getTime() - deptDate.getTime()) / (24 * 60 * 60 * 1000);
            }

            if (days < 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Return date must not be earlier than departure date");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.show();
            } else {
                String text = PlanGenerator.getPlanString(title, destination, departureDate, days, information, type, Integer.parseInt(size), addedFriendsId);
                //List<NameValuePair> params = PlanGenerator.getPlanString(title, destination, departureDate, days, information, type, Integer.parseInt(size), addedFriendsId);
                token = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString("fbAccessToken", "");
//              Log.i("before_add", destination + " " + departureDate + " duration: " + Integer.toString(days) + " info: " + information + " type: " + Integer.toString(type) + " size: " + size);
                query = text;
                Log.i("query", query);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOperation no = new NetworkOperation();
                        res = no.addPlan(token, query);

                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String msg = null;
                try {
                    msg = res.getJSONObject("err").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (msg != null && msg.equalsIgnoreCase("create success")) {
                    Log.i("flag", "success");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("Post Successfully");
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//                        }
//                    });
//                    builder.show();
                    mUIHandler.sendEmptyMessage(0);
                    findViewById(R.id.newpost_addfriend).setVisibility(View.GONE);
                }
            }
        }
    }


    public void onRadioButtonClicked(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.edit_radioGroup);
        int selectedID = rg.getCheckedRadioButtonId();

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
