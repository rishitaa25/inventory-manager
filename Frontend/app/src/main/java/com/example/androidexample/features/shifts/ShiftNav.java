package com.example.androidexample.features.shifts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.R;
import com.example.androidexample.features.chats.ChatsMain;
import com.example.androidexample.features.items.AddItem;
import com.example.androidexample.features.items.DisplayChangeHistory;
import com.example.androidexample.features.items.EditAndDeleteItem;
import com.example.androidexample.features.items.SearchItem;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

public class ShiftNav extends AppCompatActivity implements View.OnClickListener{

    private Button shiftManagementBtn, shiftViewBtn;
    private ImageView profile, menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_nav_page);

        /* button click listeners */

        shiftManagementBtn = findViewById(R.id.shiftManagementButton);
        shiftViewBtn = findViewById(R.id.shiftViewButton);

        if(SessionManager.getAccessLevel() != AccessLevel.ADMIN){
            shiftManagementBtn.setVisibility(View.GONE);
        }

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


        /* button click listeners */
        shiftManagementBtn.setOnClickListener(this);
        shiftViewBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.shiftManagementButton){
            startActivity(new Intent(ShiftNav.this, ManagerShiftView.class));
        }else if(id == R.id.shiftViewButton){
            startActivity(new Intent(ShiftNav.this, ViewShiftSchedule.class));
        }
    }


}