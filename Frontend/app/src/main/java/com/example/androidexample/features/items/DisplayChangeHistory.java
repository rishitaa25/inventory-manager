package com.example.androidexample.features.items;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.user.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayChangeHistory extends AppCompatActivity {

    // UI components
    private Button btnJsonArrReq;  // Button to trigger JSON array request
    private InventoryChangeAdapter adapter;   // Adapter for the ListView
    private ListView listView;     // ListView to display JSON data
    private TextView responseBox;
    private ImageView profile, menu;

    // URL to fetch JSON array data
    private static final String URL_JSON_ARRAY = "http://coms-3090-013.class.las.iastate.edu:8080/inventory/changes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_change_history);

        // Initialize UI elements
        btnJsonArrReq = findViewById(R.id.btnJsonArr);
        listView = findViewById(R.id.listView);
        responseBox = findViewById(R.id.responseBox);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);

        // Initialize the adapter with an empty list (data will be added dynamically)
        adapter = new InventoryChangeAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        // Set up click listener for the button
        btnJsonArrReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonArrayReq(); // Trigger JSON array request when button is clicked
            }
        });

    }

    /**
     * Method to make a JSON array request using Volley
     */
    private void makeJsonArrayReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,  // HTTP request method (GET)
                URL_JSON_ARRAY,      // URL of the JSON array API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        responseBox.setText("success");

                        // Loop through the JSON array and extract required fields
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer changeId = jsonObject.getInt("changeId");   // Extract name
                                Integer skuNumber = jsonObject.getInt("skuNumber");
                                Integer amountOfItem = jsonObject.getInt("amountOfItem");   // Extract name

                                Integer employeeId = jsonObject.optInt("employeeId",-1);// Extract email


                                // Create a ListItemObject and add it to the adapter
                                InventoryChangeObject item = new InventoryChangeObject(changeId, skuNumber, amountOfItem, employeeId);
                                adapter.add(item);

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
                        responseBox.setText(error.toString());
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
}


