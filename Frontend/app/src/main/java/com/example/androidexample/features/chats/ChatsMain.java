package com.example.androidexample.features.chats;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.examples.ListItemObject;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;
import com.google.android.material.textfield.TextInputEditText;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChatsMain extends AppCompatActivity implements ChatWebSocketListener,View.OnClickListener{


    private LinearLayout chatView;
    private TextInputEditText chatRoomInput, usernameInput;
    private Button connectButton, adminChatButton;
    private final String getChatListUrl = "";
    private ImageView profile, menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main_user_input_rooms);

        /* initialize UI elements */
        //chatView = findViewById(R.id.chatView);
        chatRoomInput = findViewById(R.id.chatRoomInputBox);
        usernameInput = findViewById(R.id.usernameInputBox);
        adminChatButton = findViewById(R.id.createChatButton);
        if(SessionManager.getAccessLevel() == AccessLevel.ADMIN){
            adminChatButton.setVisibility(View.VISIBLE);
        }

        connectButton = findViewById(R.id.connectButton);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);

        // Establish WebSocket connection and set listener
        connectButton.setOnClickListener(this);
        adminChatButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.connectButton){
            String room = String.valueOf(chatRoomInput.getText());
            ChatWebSocketManager.connectAndOpenChat(ChatsMain.this,room);

        } else if (id == R.id.createChatButton) {
            startActivity(new Intent(ChatsMain.this, AdminChatView.class));
        }
    }



    private ChatRoomObj[] getChatRoomsList(){
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,  // HTTP request method (GET)
                getChatListUrl,      // URL of the JSON array API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Loop through the JSON array and extract required fields
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("name");   // Extract name
                                String email = jsonObject.getString("email"); // Extract email

                                // Create a ListItemObject and add it to the adapter
                                ListItemObject item = new ListItemObject(name, email);
                                //adapter.add(item);


                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle JSON parsing errors
                            }
                        }
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

            // Override getParams() if you need to send request parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1"); // Example parameter
//                params.put("param2", "value2"); // Example parameter
                return params;
            }
        };

        // Adding request to the Volley request queue for execution
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
        return null;
    }



    @Override
    public void onWebSocketMessage(String message) {}

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {}

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onWebSocketError(Exception ex) {}
}


