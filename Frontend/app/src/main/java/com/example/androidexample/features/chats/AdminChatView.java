package com.example.androidexample.features.chats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.user.data.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AdminChatView extends AppCompatActivity implements View.OnClickListener {

    // UI components
    private Button createRoomButton, addEmployeeButton;
    private TextView errorMesaggeBox;
    private TextInputEditText employeeIdBox, groupIDBox, roomNameBox;
    private ImageView profile, menu;
    private static final String URL_CREATE_GC = "http://coms-3090-013.class.las.iastate.edu:8080/groupchat/create";
    private static final String URL_ADD_EMPLOYEE = "http://coms-3090-013.class.las.iastate.edu:8080/groupchat/addMember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_chat_view);

        createRoomButton = findViewById(R.id.createRoomButton);
        addEmployeeButton = findViewById(R.id.addEmployeeButton);

        employeeIdBox = findViewById(R.id.employeeIdBox);
        groupIDBox = findViewById(R.id.groupIDBox);
        roomNameBox = findViewById(R.id.roomNameBox);


        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


        errorMesaggeBox = findViewById(R.id.errorMessageView);

        /* button click listeners */
        addEmployeeButton.setOnClickListener(this);
        createRoomButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.createRoomButton) {
            String roomName = String.valueOf(roomNameBox.getText());
            int adminId = SessionManager.getUserId();

            try {

                JSONObject jsonBody = new JSONObject();

                jsonBody.put("roomName", roomName);
                jsonBody.put("adminId", adminId);

                postCreateGroupChat(jsonBody);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (id == R.id.addEmployeeButton) {
            String groupId = String.valueOf(groupIDBox.getText());
            String employeeIdStr = String.valueOf(employeeIdBox.getText());

            try {
                int employeeId = Integer.parseInt(employeeIdStr);
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("groupId", groupId);
                jsonBody.put("employeeId", employeeId);

                postAddMemberToGc(jsonBody);

            } catch (NumberFormatException e) {
                errorMesaggeBox.setText("Invalid Employee ID: must be a number");
                Log.e("AdminChatView", "Invalid employee ID: " + employeeIdStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void postCreateGroupChat(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_CREATE_GC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Created Successfully", response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            errorMesaggeBox.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        } else {
                            errorMesaggeBox.setText("Error: " + error.toString());
                        }
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void postAddMemberToGc(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_ADD_EMPLOYEE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Created Successfully", response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = new String(error.networkResponse.data);
                            Log.e("Error", "Status: " + error.networkResponse.statusCode + " Body: " + body);
                            errorMesaggeBox.setText("Error " + error.networkResponse.statusCode + ": " + body);
                        } else {
                            errorMesaggeBox.setText("Error: " + error.toString());
                        }
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
