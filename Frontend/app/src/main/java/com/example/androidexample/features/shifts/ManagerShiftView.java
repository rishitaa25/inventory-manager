package com.example.androidexample.features.shifts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aigestudio.wheelpicker.WheelPicker;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.items.SearchItem;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerShiftView extends AppCompatActivity implements View.OnClickListener{
    private Button createButton, updateButton, deleteButton, homeButton, selectStartTimeButton, selectEndTimeButton, selectDateButton;
    private TextView errorMesaggeBox, dateView, startTimeView, endTimeView;
    private CalendarView calendarView;
    private TextInputEditText employeeIDBox;
    private LocalDate selectedDate;
    private LocalTime endTime,startTime;
    private ImageView profile, menu;
    private final int START_TIME = 1;
    private final int END_TIME = 2;


    private static final String URL_DELETE_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/shift/delete/";
    private static final String URL_PUT_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/shift/update";
    private static final String URL_POST_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/shift/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_shift_view);

        createButton = findViewById(R.id.createShiftButton);
        updateButton = findViewById(R.id.updateShiftButton);
        deleteButton = findViewById(R.id.deleteShiftButton);
        selectEndTimeButton = findViewById(R.id.selectEndtimeButton);
        selectStartTimeButton = findViewById(R.id.selectStartTimeButton);
        selectDateButton = findViewById(R.id.selectDateButton);

        dateView = findViewById(R.id.dateView);
        startTimeView = findViewById(R.id.startTimeView);
        endTimeView = findViewById(R.id.endTimeView);

        employeeIDBox = findViewById(R.id.employeeIdInput);
        errorMesaggeBox = findViewById(R.id.errorMessageView2);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);

        /* button click listeners */
        createButton.setOnClickListener(this);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        selectStartTimeButton.setOnClickListener(view -> displayTimePopup(START_TIME));
        selectEndTimeButton.setOnClickListener(view -> displayTimePopup(END_TIME));
        selectDateButton.setOnClickListener(view -> displayCalendarPopup());



    }

    private void displayTimePopup(int identifier){
            //create new wheels and populate them with their respective data
            WheelPicker hoursWheel = new WheelPicker(this);
            WheelPicker minutesWheel = new WheelPicker(this);

            populateHours(hoursWheel);
            populateMinutes(minutesWheel);

            setPickerAttributes(hoursWheel);
            setPickerAttributes(minutesWheel);

            TextView colon = new TextView(this);
            colon.setText(":");
            colon.setTextSize(30);
            colon.setGravity(Gravity.CENTER);
            colon.setPadding(10, 0, 10, 0);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);


            layout.addView(hoursWheel);
            layout.addView(colon);
            layout.addView(minutesWheel);
            layout.setGravity(Gravity.CENTER);


            //build a popup for them
            new AlertDialog.Builder(this)
                    .setTitle("Pick a time")
                    .setView(layout)
                    .setPositiveButton("OK", (dialog, which) -> {
                        int selectedHour = hoursWheel.getCurrentItemPosition()+1;
                        int selectedMinute = minutesWheel.getCurrentItemPosition();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalTime time = LocalTime.of(selectedHour,selectedMinute);
                            if (identifier == START_TIME) {
                                startTimeView.setText(time.toString());
                                startTime = time;
                            } else if (identifier == END_TIME) {
                                endTimeView.setText(time.toString());
                                endTime = time;
                            }
                            Toast.makeText(this, time.toString(), Toast.LENGTH_SHORT).show();

                        }

                        })
                    .setNegativeButton("Cancel", null)
                    .show();


    }
    private void populateHours(WheelPicker picker){
        List<String> hours = new ArrayList<>();
        for (int i = 1; i <= 23; i++) hours.add(String.format("%02d", i));
        picker.setData(hours);

    }
    private void populateMinutes(WheelPicker picker){
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) minutes.add(String.format("%02d", i));
        picker.setData(minutes);

    }

    private void setPickerAttributes(WheelPicker picker){
        picker.setCurved(true);
        picker.setAtmospheric(true);
        picker.setForegroundGravity(Gravity.CENTER);
        picker.setLayoutParams(new ViewGroup.LayoutParams(200,400));
    }

    private void displayCalendarPopup(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            calendarView = new CalendarView(this);


            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {// get the selected date
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    }
                }
            });

            LinearLayout layout = new LinearLayout(this);

            layout.addView(calendarView);

            new AlertDialog.Builder(this)
                    .setTitle("Pick a date")
                    .setView(layout)
                    .setPositiveButton("OK", (dialog, which) -> {
                        Toast.makeText(this, "Date: " + selectedDate.toString(), Toast.LENGTH_SHORT).show();
                        dateView.setText(selectedDate.toString());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backToSearchButton) {
            startActivity(new Intent(ManagerShiftView.this, SearchItem.class));
        }

        String employeeID = String.valueOf(employeeIDBox.getText());

        /* input validation */
        if (selectedDate == null) {
            errorMesaggeBox.setText("Please select a date");
            return;
        }else if (employeeID.isEmpty()) {
            errorMesaggeBox.setText("Please Enter an employee ID");
        }else if(!(employeeID.matches("[0-9]+"))){
            errorMesaggeBox.setText("Employee ID may only contain numbers");
        }
        /* end input validation */

        if (id == R.id.deleteShiftButton) {
            if (employeeID.isEmpty()) {
                errorMesaggeBox.setText("Please Enter an employee ID");
            } else {
                sendDeleteRequestToDB(employeeID, selectedDate.toString());
            }
            return;//return early cuz no need to continue
        }
        //create and update logic below
        if (startTime == null || endTime == null) {
            errorMesaggeBox.setText("Please select start and end times");
            return;
        }
        String startTimeString = startTime.toString();
        String endTimeString = endTime.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime startTime = LocalTime.parse(startTimeString);
            LocalTime endTime = LocalTime.parse(endTimeString);
            try {
                JSONObject shiftJson = createShiftJson(Integer.valueOf(employeeID), startTime.toString(), endTime.toString(), selectedDate.toString());
                if (id == R.id.createShiftButton) {
                    sendPostRequestToDB(shiftJson);
                } else if (id == R.id.updateShiftButton) {
                    sendPutRequestToDB(shiftJson);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public JSONObject createShiftJson(Integer employeeId, String startTime, String endTime, String date) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("employeeId", employeeId);
        jsonBody.put("startTime", startTime);
        jsonBody.put("endTime", endTime);
        jsonBody.put("shiftDate", date);
        return jsonBody;
    }

    public void sendPostRequestToDB(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_POST_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Updated Successful", response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            errorMesaggeBox.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        } else {
                            errorMesaggeBox.setText("Error: " + error.getMessage());
                        }
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void sendDeleteRequestToDB(String employeeId, String date) {
        final String url = URL_DELETE_ITEM + date + "/from-employee/1" /*+ employeeId*/;


        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,  // HTTP request method (GET)
                url,
                // URL of the JSON object API// Pass null as the request body since it's a GET request
                // Response listener for handling successful response
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        errorMesaggeBox.setText(response);
                    }
                },

                // Error listener for handling request failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            errorMesaggeBox.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        } else {
                            //errorMesaggeBox.setText("Error: " + error.getMessage());
                        }
                    }
                }
        ) {@Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            String token = getSharedPreferences("auth", MODE_PRIVATE)
                    .getString("token", "");
            headers.put("Authorization", "Bearer " + SessionManager.getToken());
            return headers;
        }
        };

        // Adding request to the Volley request queue for execution
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public void sendPutRequestToDB(JSONObject jsonObject) {
        final String mRequestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                URL_PUT_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Updated Successfully", response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            errorMesaggeBox.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        } else {
                            errorMesaggeBox.setText("Error: " + error.toString());
                        }
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
