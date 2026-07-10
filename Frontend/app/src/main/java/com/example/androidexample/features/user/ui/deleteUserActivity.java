package com.example.androidexample.features.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.user.data.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class deleteUserActivity extends AppCompatActivity implements View.OnClickListener{
    // UI components
    private Button deleteButton, homeButton;
    private TextView errorMesaggeBox;
    private TextInputEditText employeeIdInput;
    private static final String URL_STRING_REQ = "http://coms-3090-013.class.las.iastate.edu:8080/employee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        deleteButton = findViewById(R.id.searchButton);
        homeButton = findViewById(R.id.homeButton);


        employeeIdInput = findViewById(R.id.employeeIDInput);
        errorMesaggeBox = findViewById(R.id.errorMessageView);


        /* button click listeners */
        deleteButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.searchButton) {
            String employeeID = String.valueOf(employeeIdInput.getText());
            if(!(employeeID.matches("[0-9]]"))){
                errorMesaggeBox.setText("Ids may only contain numbers");
            }
            sendDeleteRequestToDB(employeeID);

        }else if(id == R.id.homeButton){
            startActivity(new Intent(deleteUserActivity.this, MainActivity.class));

        }
    }
    private void sendDeleteRequestToDB(String s) {
        String delReq = URL_STRING_REQ + "/" + s;

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                delReq,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Account Deleted Successfully", response);
                        errorMesaggeBox.setText(response.toString());
                        //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Account Deletion Error", error.toString());
                        errorMesaggeBox.setText(error.toString());
                    }
                }
        ) {

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
