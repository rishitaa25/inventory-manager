package com.example.androidexample.features.navigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.androidexample.R;
import com.example.androidexample.features.items.ItemManagementNavPage;
import com.example.androidexample.features.notifications.ui.NotificationActivity;
import com.example.androidexample.features.shipments.ui.ShipmentListViewActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;
import com.example.androidexample.features.user.ui.EditUserActivity;
import com.example.androidexample.features.user.ui.SignupActivity;
import com.example.androidexample.features.user.ui.deleteUserActivity;
import com.example.androidexample.features.user.ui.LoginActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button strBtn, jsonObjBtn, jsonArrBtn, imgBtn, inventoryManagementNav, notificationBtn;

    private Button loginBtn, signupBtn, delBtn, btnSchedule, editBtn, btnShipmentView, btnShipmentDelete, btnShipmentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* button click listeners */

//        loginBtn = findViewById(R.id.loginButton);
//        signupBtn = findViewById(R.id.signupButton);
        delBtn = findViewById(R.id.deleteButton);
//        editBtn = findViewById(R.id.btnEditUser);
//        btnShipmentView = findViewById(R.id.btnShipmentView);
        inventoryManagementNav = findViewById(R.id.inventoryManagementNav);
        notificationBtn = findViewById(R.id.btnHome);


        /* button click listeners */
//        loginBtn.setOnClickListener(this);
//        signupBtn.setOnClickListener(this);
        delBtn.setOnClickListener(this);
//        editBtn.setOnClickListener(this);
//        btnShipmentView.setOnClickListener(this);
        inventoryManagementNav.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.deleteButton) {
            startActivity(new Intent(MainActivity.this, deleteUserActivity.class));
        } else if (id == R.id.inventoryManagementNav) {
            startActivity(new Intent(MainActivity.this, ItemManagementNavPage.class));
        } else if(id == R.id.btnHome) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        }
    }
}