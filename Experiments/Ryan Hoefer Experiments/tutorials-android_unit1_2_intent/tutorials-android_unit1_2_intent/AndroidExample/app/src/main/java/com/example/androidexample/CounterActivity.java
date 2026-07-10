package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button increaseBtn1; // define increase button variable
    private Button decreaseBtn1; // define decrease button variable
    private Button increaseBtn10; // define increase button variable
    private Button decreaseBtn10; // define decrease button variable
    private Button backBtn;     // define back button variable
    private TextView displayText;
    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

    /*Initialize UI Elements*/
        numberTxt = findViewById(R.id.number);
        increaseBtn1 = findViewById(R.id.counter_increase_btn);
        decreaseBtn1 = findViewById(R.id.counter_decrease_btn);
        increaseBtn10 = findViewById(R.id.counter_increase_by_10_btn);
        decreaseBtn10 = findViewById(R.id.counter_decrease_by_10_btn);
        backBtn = findViewById(R.id.counter_back_btn);
        displayText = findViewById(R.id.text_display);

        /* Have seperate listener logic for each button clicked*/
        View.OnClickListener tempCounterListener = v -> {
            int increment = 0;
            int viewId = v.getId();
            if(viewId == R.id.counter_decrease_btn){
                increment = -1;
            } else if(viewId == R.id.counter_decrease_by_10_btn){
                increment = -10;
            }else if(viewId == R.id.counter_increase_btn){
                increment = 1;
            }else if(viewId == R.id.counter_increase_by_10_btn){
                increment = 10;
            }

            counter += increment;//incrememt counter by the specified logic
            String text = String.valueOf(counter) + "\u00B0" + "C";
            numberTxt.setText(text);
            displayText.setText(getTempDisplayString(counter));//get the text display based on what the temperature is at

            /*switch to screen if the temp is too high or too low*/
            if(counter >= 200){
                Intent intent = new Intent(CounterActivity.this, UnderworldScreen.class);
                startActivity(intent);
            }
            if(counter <= -273){
                Intent intent = new Intent(CounterActivity.this, Space.class);
                startActivity(intent);
            }
        };

        /*set the listener for each button*/
        increaseBtn1.setOnClickListener(tempCounterListener);
        increaseBtn10.setOnClickListener(tempCounterListener);
        decreaseBtn1.setOnClickListener(tempCounterListener);
        decreaseBtn10.setOnClickListener(tempCounterListener);


        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CounterActivity.this, MainActivity.class);
            intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
            startActivity(intent);
        });

    }
    public String getTempDisplayString(int temp){
        if(temp > 200){ return "Uh Oh";}
        else if(temp > 150){ return "Warning!!!! APPROACHING MAXIMUM TEMPERATURE";}
        else if(temp > 100){ return "Boiling Hot!";}
        else if(temp > 60){ return "Extremely Hot!";}
        else if(temp > 40){ return "Very Hot";}
        else if(temp > 30){ return "Hot";}
        else if(temp > 18){ return "Room Temperature";}
        else if(temp > 0){ return "Cold";}
        else if(temp > -32){ return "Freezing";}
        else if(temp > -100){ return "Super Cold";}
        else if(temp > -150){ return "Extremely Cold";}
        else if(temp > -200){ return "Warning!!!! APPROACHING Minimum TEMPERATURE";}
        else if (temp >= -273){ return "Absolute Zero";}
        else return "error";
    }
}