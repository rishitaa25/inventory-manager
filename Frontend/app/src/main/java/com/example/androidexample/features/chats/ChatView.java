package com.example.androidexample.features.chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.user.data.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ChatActivity handles the chat interface where users can send and receive messages
 * using a WebSocket connection.
 */
public class ChatView extends AppCompatActivity implements ChatWebSocketListener {
    private int messageCount = 0;
    private Button sendBtn,homeBtn;
    private TextInputEditText msgEtx;
    private TextView msgTv;
    private LinearLayout chatView;
    private ScrollView chatScrollView;
    private String username,room;
    private final String GET_CHAT_HISTORY_URL = "http://coms-3090-013.class.las.iastate.edu:8080/chat/history/";
    private final String chatServerURL = "ws://coms-3090-013.class.las.iastate.edu:8080/chat/";
    private ArrayList<ChatObj> chatHistory = new ArrayList<ChatObj>();
    private ImageView profile, menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        msgEtx =  findViewById(R.id.msgEdt);
        chatScrollView = findViewById(R.id.chatScrollView);
        chatView = findViewById(R.id.chatView);
        msgTv = findViewById(R.id.msgTv);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


        room = getIntent().getStringExtra("room");

        /* connect this activity to the websocket instance */
        ChatWebSocketManager.getInstance().setWebSocketListener(ChatView.this);
        username = SessionManager.getUsername();
        String url = chatServerURL + room + "/" + username;
        ChatWebSocketManager.getInstance().connectWebSocket(url);


        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                ChatWebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
                //clear text box once the msg is sent
                msgEtx.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
        getChatHistory();
    }


    /**
     * Called when a message is received from the WebSocket.
     * This method ensures that UI updates happen on the main thread.
     */
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    300,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if(message.substring(0,message.indexOf(':')).equals(username)){
                params.gravity = Gravity.END;
            }


            textView.setLayoutParams(params);
            String messagePlusCount = ++messageCount + " - " + message;

            textView.setText(messagePlusCount);
            chatView.addView(textView);

            /*
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
            */

        });
    }

    private void getChatHistory() {
        String url = GET_CHAT_HISTORY_URL + room;



        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,  // HTTP request method (GET)
                url,      // URL of the JSON array API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        //String respo = "Volley Response: " + response.toString();
                        //msgTv.setText(respo);

                        // Loop through the JSON array and extract required fields
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Long id = jsonObject.getLong("id");
                                String sender = jsonObject.getString("sender");
                                String room = jsonObject.getString("room");
                                String content = jsonObject.getString("content");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    LocalDateTime timestamp = LocalDateTime.parse(jsonObject.getString("timestamp"));
                                    ChatObj chatMessage = new ChatObj(id,sender,room,content,timestamp);
                                    chatHistory.add(chatMessage);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle JSON parsing errors
                            }
                        }
                        displayChatHistory();

                    }
                },

                // Error listener for handling request failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            msgTv.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        }
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
    }
    private void displayChatHistory(){


        runOnUiThread(() -> {
            for(ChatObj message : chatHistory) {


                TextView textView = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        300,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                if(message.getSender().equals(username)){
                    params.gravity = Gravity.END;
                }

                String messagePlusCount = ++messageCount + " - " + message.getSender() + ": " + message.getContent();

                textView.setLayoutParams(params);

                textView.setText(messagePlusCount);
                chatView.addView(textView);
            }
            msgTv.setText(room);

            /*
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
            */

        });


    }

    /**
     * Called when the WebSocket connection is closed.
     * Displays the closure reason in the TextView.
     *
     * @param code   The status code of the closure
     * @param reason The reason provided for closure
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+ handshakedata.getHttpStatusMessage());
        });
    }


    @Override
    public void onWebSocketError(Exception ex) {}
}