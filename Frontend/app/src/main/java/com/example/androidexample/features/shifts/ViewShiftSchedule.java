package com.example.androidexample.features.shifts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.features.user.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewShiftSchedule extends AppCompatActivity implements View.OnClickListener{

    private Button forwardWeekButton, backwardWeekButton;

    private static String url = "http://coms-3090-013.class.las.iastate.edu:8080/employee/";
    private Calendar calendar;
    private LocalDate todaysDate,startOfWeek,endOfWeek;
    private LinearLayout shiftContainer;
    private ImageView profile, menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shift_schedule);



        shiftContainer = findViewById(R.id.shiftContainer);
        forwardWeekButton = findViewById(R.id.forwardWeekButton);
        backwardWeekButton = findViewById(R.id.backwardWeekButton);

        /* button click listeners */
        forwardWeekButton.setOnClickListener(this);
        backwardWeekButton.setOnClickListener(this);


        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);



        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todaysDate = LocalDate.of(year, month, day);
            startOfWeek = todaysDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            endOfWeek = todaysDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            setShiftSchedule(startOfWeek.toString(),endOfWeek.toString());

        }




    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.forwardWeekButton){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startOfWeek = startOfWeek.plusWeeks(1);
                endOfWeek = endOfWeek.plusWeeks(1);
                setShiftSchedule(startOfWeek.toString(), endOfWeek.toString());
            }

        }else if(id == R.id.backwardWeekButton){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startOfWeek = startOfWeek.minusWeeks(1);
                endOfWeek = endOfWeek.minusWeeks(1);
                setShiftSchedule(startOfWeek.toString(), endOfWeek.toString());
            }

        }
    }

    private void displayShiftSchedule(List<ShiftObject> shiftArr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shiftContainer.removeAllViews(); // clear screen

            LinearLayout hourColumn = new LinearLayout(this);
            hourColumn.setOrientation(LinearLayout.VERTICAL);
            hourColumn.setLayoutParams(new LinearLayout.LayoutParams(100,ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView hourBuffer = new TextView(this);
            LinearLayout.LayoutParams hourBufferParams = new LinearLayout.LayoutParams(100,80);
            hourBuffer.setLayoutParams(hourBufferParams);
            hourColumn.addView(hourBuffer);

            for (int i = 0; i < 24; i++){
                TextView hour = new TextView(this);
                hour.setText(String.valueOf(i));
                LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(100,69);
                hourParams.setMargins(2,2,2,2);
                hour.setLayoutParams(hourParams);
                hourColumn.addView(hour);

            }
            shiftContainer.addView(hourColumn);



            for (ShiftObject shift : shiftArr) {
                if (shift == null) continue;
                LinearLayout column = new LinearLayout(this);
                column.setOrientation(LinearLayout.VERTICAL);

                column.setLayoutParams(new LinearLayout.LayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                shiftContainer.addView(column);

                TextView dayLabel = new TextView(this);
                dayLabel.setText(shift.getDay());
                dayLabel.setLayoutParams(new LinearLayout.LayoutParams(125, 69));
                column.addView(dayLabel);

                LocalTime currentTime = LocalTime.of(0, 0);
                LocalTime startTime = shift.getStartTime();
                LocalTime endTime = shift.getEndTime();


                for (int i = 1; i < 24; i++) {//hardcoded 24 for now
                    TextView hourBlock = new TextView(this);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(75, 75);
                    params.setMargins(2, 2, 2, 2);
                    hourBlock.setLayoutParams(params);
                    hourBlock.setBackgroundColor(Color.LTGRAY);

                    if (startTime != null && endTime != null) {
                        if (!(currentTime.isBefore(startTime)) && currentTime.isBefore(endTime)) {
                            hourBlock.setBackgroundColor(Color.parseColor("#4A0080"));
                        }

                    }
                    currentTime = currentTime.plusHours(1);
                    column.addView(hourBlock);


                }
            }


        }
    }

    private void setShiftSchedule(String startOfWeekDate, String endOfWeekDate) {
       //url += startOfWeekDate + "to/" + endOfWeekDate;
        String setUrl = url + SessionManager.getUserId() +  "/weekly-shifts/" + startOfWeekDate;

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,  // HTTP request method (GET)
                setUrl,      // URL of the JSON array API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ShiftObject> shiftList = new ArrayList<>();
                        Log.d("Volley Response", response.toString());

                        // Loop through the JSON array and extract required fields
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String startTime = jsonObject.optString("startTime", "00:00:00");
                                String endTime = jsonObject.optString("endTime", "00:00:00");
                                String day = jsonObject.optString("shiftDate", "Unknown");   // Extract name
                                int duration = jsonObject.optInt("duration", 0);



                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    try {
                                        LocalTime start = LocalTime.parse(startTime);
                                        LocalTime end = LocalTime.parse(endTime);
                                        shiftList.add(new ShiftObject(start, end, day, duration));
                                    } catch (DateTimeParseException e) {
                                        Log.e("ParseError", "Time format invalid: " + startTime);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle JSON parsing errors
                            }
                        }
                        displayShiftSchedule(shiftList);



                    }
                },

                // Error listener for handling request failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString()); // Log error details
                    }
                }
        ) {
            // Override getHeaders() if authentication headers are needed
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

        };

        // Adding request to the Volley request queue for execution
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
}

