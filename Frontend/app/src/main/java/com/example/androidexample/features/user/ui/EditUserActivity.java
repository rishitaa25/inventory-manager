package com.example.androidexample.features.user.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class is used to edit a user's information.
 *
 * It allows user's with ADMIN permissions to edit an user's information.
 * It takes the ssn of the employee whose information will be changed. The user
 * cannot change the ssn, but can change the firstname, lastname, access level,
 * username, email, and phone number.
 *
 * This class uses HTTP PUT requests to communicate with the backend server.
 *
 * @author Olivia Blais
 */
public class EditUserActivity extends AppCompatActivity {
    private EditText userSSN, username, accessLevel, email, phone, firstName, lastName, id;
    private ImageView profile, menu, updateUser, cancelButton;
    private TextView messageResponse;
    private boolean isUpdateConfirmed = false;


    private static final String UPDATE_URL =
            //"http://10.0.2.2:8080/";
            //http://10.0.2.2:8080/employee/{ssn}
            "http://coms-3090-013.class.las.iastate.edu:8080/";

    /**
     * Initializes the Edit user activity and handles user input.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firstName = findViewById(R.id.user_first_name);
        lastName = findViewById(R.id.user_last_name);
//        username = findViewById(R.id.user_username);
//        accessLevel = findViewById(R.id.user_access_level);
        email = findViewById(R.id.user_email);
        phone = findViewById(R.id.user_phone);
//        userSSN = findViewById(R.id.user_SSN);
        updateUser = findViewById(R.id.update_icon);
        messageResponse = findViewById(R.id.edit_user_msg);
        cancelButton = findViewById(R.id.cancel_icon);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        id = findViewById(R.id.user_id);


        BannerHelper.setupBanner(this, profile, menu);


            updateUser.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (!isUpdateConfirmed){
                        // First click → ask for confirmation
                        messageResponse.setText("Are you sure you want to update? Click update again to confirm.");
                        isUpdateConfirmed = true;
                        return;
                    }

                    String stringID = id.getText().toString();

                    if (stringID.isEmpty()) {
                        messageResponse.setText("Please enter ID.");
                        return;
                    }

                    int ssn;
                    try {
                        ssn = Integer.parseInt(stringID);
                    } catch (NumberFormatException e) {
                        messageResponse.setText("ID must be a number.");
                        return;
                    }

                    messageResponse.setText("Updating user...");
                    if (SessionManager.getAccessLevel() == AccessLevel.ADMIN) {
                        requestUserUpdate(ssn);
                    } else {
                        makeUserChangeRequest();
                    }
                }
            });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUserActivity.this, UserProfileActivity.class));
            }
        });
    }

    /**
     * Sends a PUT request to the backend server to update an employee's information.
     *
     * The request body is constructed as a JSON object containing only the fields
     * that the user has modified (non-empty inputs). The SSN is appended to the
     * request URL to identify the specific employee to update.
     *
     * If successful, a confirmation message is displayed to the user. If it fails,
     * an error message is shown and the error is logged.
     *
     * @param ssn the SSN of the employee whose information is being updated
     */
    private void requestUserUpdate(int ssn) {
        JSONObject requestBody = new JSONObject();
        try {
//            requestBody.put("ssn", ssn);

            String idVal = id.getText().toString().trim();
            if (!idVal.isEmpty()) {
                requestBody.put("employeeId", Integer.parseInt(idVal));
            }

//            String usernameVal = username.getText().toString().trim();
//            if (!usernameVal.isEmpty()) {
//                requestBody.put("username", usernameVal);
//            }

            String firstNameVal = firstName.getText().toString().trim();
            if (!firstNameVal.isEmpty()) {
                requestBody.put("firstName", firstNameVal);
            }

            String lastNameVal = lastName.getText().toString().trim();
            if (!lastNameVal.isEmpty()) {
                requestBody.put("lastName", lastNameVal);
            }

//            String accessVal = accessLevel.getText().toString().trim();
//            if (!accessVal.isEmpty()) {
//                try {
//                    AccessLevel.valueOf(accessVal.toUpperCase());
//                    requestBody.put("accessLevel", accessVal.toUpperCase());
//                } catch (IllegalArgumentException e) {
//                    Log.e("UpdateUser", "Invalid access level");
//                    messageResponse.setText("Invalid access level.");
//                    return;
//                }
//            }

            String emailVal = email.getText().toString().trim();
            if (!emailVal.isEmpty()) {
                requestBody.put("email", emailVal);
            }

            String phoneVal = phone.getText().toString().trim();
            if (!phoneVal.isEmpty()) {
                requestBody.put("phoneNumber", phoneVal);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("REQUEST_BODY", requestBody.toString());
        Log.d("REQUEST_URL", UPDATE_URL + "admin/" + "update");
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                UPDATE_URL + "admin/" + "update",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response.toString());

                            messageResponse.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("SERVER_RESPONSE", new String(error.networkResponse.data));
                        }
                        Log.e("Volley Error", error.toString());
                        messageResponse.setText("Update failed. Please try again.");
                    }
                }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    Log.d("SHIPEMENTS_TOKEN", SessionManager.getToken());
                    headers.put("Authorization", "Bearer " + SessionManager.getToken());
    //                headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

        };

        Volley.newRequestQueue(this).add(request);
    }

    private void makeUserChangeRequest(){
        JSONObject requestBody = new JSONObject();
        try {

            String idVal = id.getText().toString().trim();
            if (!idVal.isEmpty()) {
                requestBody.put("employeeId", Integer.parseInt(idVal));
            }

            String firstNameVal = firstName.getText().toString().trim();
            if (!firstNameVal.isEmpty()) {
                requestBody.put("firstName", firstNameVal);
            }

            String lastNameVal = lastName.getText().toString().trim();
            if (!lastNameVal.isEmpty()) {
                requestBody.put("lastName", lastNameVal);
            }

            String emailVal = email.getText().toString().trim();
            if (!emailVal.isEmpty()) {
                requestBody.put("email", emailVal);
            }

            String phoneVal = phone.getText().toString().trim();
            if (!phoneVal.isEmpty()) {
                requestBody.put("phoneNumber", phoneVal);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("REQUEST_BODY", requestBody.toString());
        Log.d("REQUEST_URL", UPDATE_URL + "employee/" + "update");
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                UPDATE_URL + "employee/" + "update",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response.toString());

                        messageResponse.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("SERVER_RESPONSE", new String(error.networkResponse.data));
                        }
                        Log.e("Volley Error", error.toString());
                        messageResponse.setText("Request failed. Please try again.");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                Log.d("SHIPEMENTS_TOKEN", SessionManager.getToken());
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

        };

        Volley.newRequestQueue(this).add(request);
    }

}
