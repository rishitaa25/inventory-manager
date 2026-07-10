package com.example.androidexample.features.navigation.Services;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.user.ui.EditUserActivity;
import com.example.androidexample.features.user.ui.UserProfileActivity;

public class BannerHelper {

    public static void setupBanner(Context context, ImageView profile, ImageView menu) {

        if (profile != null) {
            profile.setOnClickListener(v -> {
                context.startActivity(new Intent(context, UserProfileActivity.class));
            });
        }

        if (menu != null) {
            menu.setOnClickListener(v -> {
                context.startActivity(new Intent(context, HomeActivity.class));
            });
        }
    }
}