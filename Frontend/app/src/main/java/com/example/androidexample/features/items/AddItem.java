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

public class AddItem extends AppCompatActivity implements View.OnClickListener {
    private int confirmation; //confirms user wants 0 items;
    // UI components
    private Button editItemButton, addItem;
    private TextView errorMesaggeBox;
    private TextInputEditText itemIdBox, itemNameBox, itemNumberBox, itemDesciptionBox;
    private ImageView profile, menu;
    private static final String URL_ADD_REQ = "http://coms-3090-013.class.las.iastate.edu:8080/inventory/add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_item);

        editItemButton = findViewById(R.id.backToSearchButton);

        addItem = findViewById(R.id.addItemButton);
        itemNameBox = findViewById(R.id.itemNameBox);
        itemIdBox = findViewById(R.id.itemIDBox);
        itemNumberBox = findViewById(R.id.itemNumberBox);
        itemDesciptionBox = findViewById(R.id.itemDescriptionBox);

        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


        errorMesaggeBox = findViewById(R.id.errorMessageView);

        /* button click listeners */
        editItemButton.setOnClickListener(this);
        addItem.setOnClickListener(this);

        confirmation = 0;



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addItemButton) {
            String itemID = String.valueOf(itemIdBox.getText());
            String itemName = String.valueOf(itemNameBox.getText());
            String itemNumber = String.valueOf(itemNumberBox.getText());
            String itemDescription = String.valueOf(itemDesciptionBox.getText());


            if (itemID.isEmpty() && itemName.isEmpty()) {
                errorMesaggeBox.setText("Please enter either an id or a name");
            } else if (itemName.isEmpty()) {
                errorMesaggeBox.setText("Please add an item name");
            }else if(Integer.valueOf(itemID) == 0 && confirmation == 0){
                errorMesaggeBox.setText("Are you sure you want to set the item amount to 0? Click again if so.");
                confirmation = 1;
                //TODO more input validation
            }
            else {
                try {
                    if(confirmation ==1){
                        itemNumber = "0";
                    }

                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("skuNumber", Integer.valueOf(itemID));
                    jsonBody.put("itemName", itemName);
                    jsonBody.put("itemDescription", itemDescription);
                    jsonBody.put("amountOfItem", Integer.valueOf(itemNumber));


                    sendItemRequestToDB(jsonBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.backToSearchButton) {
            startActivity(new Intent(AddItem.this, SearchItem.class));

        } else if (id == R.id.homeButton) {
            startActivity(new Intent(AddItem.this, MainActivity.class));
        }
    }


    private void sendItemRequestToDB(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_ADD_REQ,
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


