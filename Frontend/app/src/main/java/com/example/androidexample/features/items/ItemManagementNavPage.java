package com.example.androidexample.features.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.features.shifts.ManagerShiftView;
import com.example.androidexample.R;

import com.example.androidexample.features.shifts.ViewShiftSchedule;
import com.example.androidexample.features.chats.ChatsMain;

public class ItemManagementNavPage extends AppCompatActivity implements View.OnClickListener{

    private Button addItemBtn, searchButton, editBtn, changeHistoryButton;

    private ImageView profile, menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management_nav_page);

        /* button click listeners */
        editBtn = findViewById(R.id.editOrDeleteItemButton);
        addItemBtn = findViewById(R.id.addItemButton);
        searchButton = findViewById(R.id.searchButton);
        changeHistoryButton = findViewById(R.id.changeHistoryButton);


        /* button click listeners */
        addItemBtn.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        changeHistoryButton.setOnClickListener(this);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.addItemButton) {
            startActivity(new Intent(ItemManagementNavPage.this, AddItem.class));
        } else if (id == R.id.editOrDeleteItemButton) {
            startActivity(new Intent(ItemManagementNavPage.this, EditAndDeleteItem.class));
        } else if (id == R.id.searchButton) {
            startActivity(new Intent(ItemManagementNavPage.this, SearchItem.class));
        } else if (id == R.id.changeHistoryButton) {
            startActivity(new Intent(ItemManagementNavPage.this, DisplayChangeHistory.class));
        }
    }


}
