package com.example.androidexample.features.navigation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;
import com.example.androidexample.features.user.ui.LoginActivity;
import com.example.androidexample.features.user.ui.SignupActivity;

public class LaunchActivity extends AppCompatActivity {

    // UI components
    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);

        // Initialize the UI components
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(view -> {
            // Handle login button click
            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
        });

        signupButton.setOnClickListener(view -> {
            // Handle signup button click
            startActivity(new Intent(LaunchActivity.this, SignupActivity.class));
        });
    }
}
