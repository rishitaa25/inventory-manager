package com.example.androidexample.features.items;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.user.data.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * for post
 * {
 * skuNumber:
 * itemName:
 * ItemDescription:
 * amountOfItem:
 * }
 *
 * then for put, ill send a change object with
 * {
 * changeID:
 * skuNumber:
 * amount:
 * employeeID:
 * }
 */
public class SearchItem extends AppCompatActivity implements View.OnClickListener {

    // UI components
    private Button editItemButton, searchButton, backToAddItemButton;
    private TextView errorMesaggeBox;
    private TextInputEditText itemIdBox, itemNameBox, itemAmountBox, itemDescriptionBox;

    private static final String URL_GET_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/inventory/{sku}";

    private ImageView profile, menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        searchButton = findViewById(R.id.searchItemButton);
        backToAddItemButton = findViewById(R.id.addAnItemButton);
        editItemButton = findViewById(R.id.backToSearchButton);


        itemNameBox = findViewById(R.id.itemNameBox);
        itemIdBox = findViewById(R.id.itemIDBox);
        itemAmountBox = findViewById(R.id.itemNumberBox);
        itemDescriptionBox = findViewById(R.id.ItemDescriptionBox);


        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);

        errorMesaggeBox = findViewById(R.id.errorMessageView);

        /* button click listeners */
        editItemButton.setOnClickListener(this);
        backToAddItemButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.searchItemButton) {
            String itemName = String.valueOf(itemNameBox.getText());
            String skuNumber = String.valueOf(itemIdBox.getText());


            if (itemName.isEmpty()) {
                errorMesaggeBox.setText("Item Name Is Empty");

            } else if (skuNumber.isEmpty()) {
                errorMesaggeBox.setText("Sku Number Is Empty");
            }  else {
                    getItemFromDB(skuNumber);


            }

        } else if (id == R.id.backToSearchButton) {
            startActivity(new Intent(SearchItem.this, EditAndDeleteItem.class));

        }else if (id == R.id.addAnItemButton){
            startActivity(new Intent(SearchItem.this, AddItem.class));
        }
    }

    private void getItemFromDB(String sku) {
        String url = URL_GET_ITEM.replace("{sku}", sku);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,  // HTTP request method (GET)
                url,     // URL of the JSON object API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {

                            String skuNumber = response.getString("skuNumber");
                            String itemName = response.getString("itemName");
                            String itemDescription = response.getString("itemDescription");
                            String amountOfItem = response.getString("amountOfItem");

                            // Populate TextViews with the retrieved data
                            itemAmountBox.setText(amountOfItem);
                            itemNameBox.setText(itemName);
                            itemIdBox.setText(skuNumber);
                            itemDescriptionBox.setText(itemDescription);


                        } catch (JSONException e) {
                            Log.e("Error", e.toString());
                        }
                    }
                },

                // Error listener for handling request failure
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
            // Override getHeaders() if authentication headers are needed
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getSharedPreferences("auth", MODE_PRIVATE)
                        .getString("token", "");
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }

            // Override getParams() if you need to send request parameters (for POST/PUT)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1"); // Example parameter
//                params.put("param2", "value2"); // Example parameter
                return params;
            }
        };

        // Adding request` to the Volley request queue for execution
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
}
