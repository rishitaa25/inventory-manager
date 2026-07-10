package com.example.androidexample.features.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.R;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.navigation.ui.LaunchActivity;
import com.example.androidexample.features.notifications.websocket.NotificationWebsocketService;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    // Ui Components
    private ImageView profile, menu, editUser, logout;
    private TextView userId, username, logoutMessage;
    private boolean  logoutConfirmed = false;

    private static final String BASE_URL = "http://coms-3090-013.class.las.iastate.edu:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        editUser = findViewById(R.id.edit_icon);
        userId = findViewById(R.id.detail_user_id);
        username = findViewById(R.id.detail_username);
        logout = findViewById(R.id.logout_icon);
        logoutMessage = findViewById(R.id.logout_message);


        // Hide profile icon
        profile.setVisibility(View.GONE);

        // set banner
        BannerHelper.setupBanner(this, profile, menu);

        // User actions
        editUser.setOnClickListener(view -> {
                startActivity(new Intent(UserProfileActivity.this, EditUserActivity.class));
        });

        logout.setOnClickListener(view -> {
            if (!logoutConfirmed) {
                logoutMessage.setText("Are you sure you want to log out?");
                logoutMessage.setBackground(getResources().getDrawable(R.drawable.background_text_blue));
                logoutMessage.setTextColor(getResources().getColor(R.color.im_sky_blue));
                new Handler().postDelayed(() -> {
                    logoutMessage.setText("");
                    logoutMessage.setBackgroundResource(0);
                }, 2000);
                logoutConfirmed = true;
                return;
            }

            // once confirmed, make logout request
            try {
                makeLogoutRequest();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });


        // User Info
        userId.setText(String.valueOf(SessionManager.getUserId()));
        username.setText(SessionManager.getUsername());

    }

        /**
         * Sends a POST request to the server to attempt a logging out.
         *
         *
         * If the logout is successful:
         * - SessionManager will be cleared
         * - it will display the success message.
         * - the app will redirect to the launch page
         *
         * Otherwise, an error message is displayed and logged.
         */
        private void makeLogoutRequest() throws JSONException {

        Log.d("LOGOUT", "Preparing request...");


        // decide which url to use
        String ENDPOINT = "";
        if (SessionManager.getAccessLevel() == AccessLevel.ADMIN)
            ENDPOINT = "admin/";
        else if (SessionManager.getAccessLevel() == AccessLevel.EMPLOYEE)
            ENDPOINT = "employee/";
        else if (SessionManager.getAccessLevel() == AccessLevel.DRIVER)
            ENDPOINT = "driver/";


        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + ENDPOINT + "logout",

                response -> {
                    Log.d("LOGOUT_RAW_RESPONSE", response.toString());

                    logoutMessage.setBackground(getResources().getDrawable(R.drawable.background_text_blue));
                    logoutMessage.setTextColor(getResources().getColor(R.color.im_sky_blue));
                    logoutMessage.setText(response.toString());

                    // Stop the websocket service and clear the session
                    stopService(new Intent(this, NotificationWebsocketService.class));
                    SessionManager.clearSession();

                    new Handler().postDelayed(() ->
                        startActivity(new Intent(UserProfileActivity.this, LaunchActivity.class)), 1000);

                },

                error -> {
                    Log.e("LOGOUT_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("LOGOUT_STATUS_CODE", String.valueOf(error.networkResponse.statusCode));
                    }
                }


        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                //Log.d("LOGOUT_TOKEN", SessionManager.getToken());
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
//                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        Volley.newRequestQueue(this).add(request);
    }

    private void makeUserInfoRequest(){
        // TODO make request to get user info
    }

}


