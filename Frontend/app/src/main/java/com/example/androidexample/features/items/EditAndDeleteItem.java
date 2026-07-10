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

public class EditAndDeleteItem extends AppCompatActivity implements View.OnClickListener{
    private Button editItemButton, deleteItemButton, backToSearchItemButton, scanButton;
    private TextView errorMesaggeBox;
    private TextInputEditText itemIdBox, itemNameBox, itemAmountBox, itemDescriptionBox, employeeIDBox, changeIDBox;
    private ImageView profile, menu;

    private static final String URL_DELETE_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/inventory/delete/{sku}";
    private static final String URL_EDIT_ITEM = "http://coms-3090-013.class.las.iastate.edu:8080/inventory/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemButton = findViewById(R.id.addItemButton);
        deleteItemButton = findViewById(R.id.deleteItemButton);
        backToSearchItemButton = findViewById(R.id.backToSearchButton);
        scanButton = findViewById(R.id.scanForItem);

        //banner setup
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        BannerHelper.setupBanner(this, profile, menu);


        itemNameBox = findViewById(R.id.itemNameBox);
        itemIdBox = findViewById(R.id.itemIDBox);
        if(getIntent().hasExtra("id")){
            itemIdBox.setText(getIntent().getStringExtra("id"));
        }
        itemAmountBox = findViewById(R.id.itemNumberBox);
        itemDescriptionBox = findViewById(R.id.itemDescriptionBox);
        employeeIDBox = findViewById(R.id.employeeIDBox);
        changeIDBox = findViewById(R.id.changeIDBox);

        errorMesaggeBox = findViewById(R.id.errorMessageView);

        /* button click listeners */
        editItemButton.setOnClickListener(this);
        deleteItemButton.setOnClickListener(this);
        backToSearchItemButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);

    }

    /**
     * {
     * changeID:
     * skuNumber:
     * amountOfItem:
     * employeeID:
     * }
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.addItemButton){
            JSONObject jsonBody = new JSONObject();
            String itemID = String.valueOf(itemIdBox.getText());
            //String itemName = String.valueOf(itemNameBox.getText());
            String itemNumber = String.valueOf(itemAmountBox.getText());
            //String itemDescription = String.valueOf(itemDescriptionBox.getText());
            String changeID = String.valueOf(changeIDBox.getText());
            String employeeID = String.valueOf(employeeIDBox.getText());
            if(itemID.isEmpty()) {
                errorMesaggeBox.setText("Please Enter a sku Number");
            }else if(itemNumber.isEmpty()) {
                errorMesaggeBox.setText("Please Enter an Item Number");
            }else if(changeID.isEmpty()) {
                errorMesaggeBox.setText("Please Enter a change ID");
            }else if(employeeID.isEmpty()) {
                errorMesaggeBox.setText("Please Enter an employee ID");
            }else {
                try {
                    jsonBody.put("changeID", Integer.valueOf(changeID));
                    jsonBody.put("skuNumber", Integer.valueOf(itemID));
                    jsonBody.put("amountOfItem", Integer.valueOf(itemNumber));
                    jsonBody.put("employeeID", Integer.valueOf(employeeID));
                    sendEditRequestToDB(jsonBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(id == R.id.deleteItemButton) {

            String itemID = String.valueOf(itemIdBox.getText());
            if (itemID.isEmpty()){
                errorMesaggeBox.setText("Please Enter a sku Number");
            } else{

                    sendDeleteRequestToDB(itemID);

        }
        }else if (id == R.id.backToSearchButton){
            startActivity(new Intent(EditAndDeleteItem.this, SearchItem.class));
        }
        else if (id == R.id.scanForItem){
            startActivity(new Intent(EditAndDeleteItem.this, BarcodeScanner.class));
        }
    }


    private void sendDeleteRequestToDB(String sku) {
        String url = URL_DELETE_ITEM.replace("{sku}", sku);


        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Deleted Successfully" , response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Error Deleting Item", error.toString());
                        errorMesaggeBox.setText(error.toString());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
/*
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
*/
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

    private void sendEditRequestToDB(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                URL_EDIT_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Item Updated Successfully", response);
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
