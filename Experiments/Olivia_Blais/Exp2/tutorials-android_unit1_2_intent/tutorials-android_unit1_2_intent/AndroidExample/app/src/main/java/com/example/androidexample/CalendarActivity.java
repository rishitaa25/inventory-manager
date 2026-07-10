package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends AppCompatActivity{
    private TextView messageText;     // define message textview variable
    private Button counterButton;     // define counter button variable
    private TextView date;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);             // link to Main activity XML

        /* initialize UI elements */
        date = findViewById(R.id.main_date_txt);
        time = findViewById(R.id.time_txt);


        long currentTime = Calendar.getInstance().getTimeInMillis();
        Date currentDate = Calendar.getInstance().getTime();

        time.setText(String.valueOf(currentTime));
        date.setText(String.valueOf(currentDate));



    }
}
