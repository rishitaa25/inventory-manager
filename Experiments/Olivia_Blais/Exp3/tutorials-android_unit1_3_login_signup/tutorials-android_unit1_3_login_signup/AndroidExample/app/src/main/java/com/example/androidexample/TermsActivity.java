package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TermsActivity extends AppCompatActivity{

    private TextView termsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        termsMessage = findViewById(R.id.main_msg_txt);

        termsMessage.setText("Terms and Conditions");
     }

}
