package com.example.androidexample.features.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.navigation.ui.MainActivity;
import com.example.androidexample.R;
import com.example.androidexample.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    // UI components
    private Button signupButton, loginbutton;
    private TextView errorMesaggeBox;
    private TextInputEditText passwordInputBox, passwordInputBox2, usernameInputBox, nameInputBox, emailInputBox, phoneNumberInputBox, ssnInputBox;
    private static final String URL_STRING_REQ = "http://coms-3090-013.class.las.iastate.edu:8080/employee/signup";

    /**
     * Creates and initializes the ui elements from the xml fiel DOM
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        signupButton = findViewById(R.id.signupButton);
        loginbutton = findViewById(R.id.loginButton);

        passwordInputBox = findViewById(R.id.passwordInput);
        passwordInputBox2 = findViewById(R.id.passwordInput2);
        nameInputBox = findViewById(R.id.nameInput);
        emailInputBox = findViewById(R.id.emailInput);
        phoneNumberInputBox = findViewById(R.id.phoneNumberInput);
        ssnInputBox = findViewById(R.id.ssnInput);

        usernameInputBox = findViewById(R.id.usernameInput);
        errorMesaggeBox = findViewById(R.id.errorMessageView);




        /* button click listeners */
        signupButton.setOnClickListener(this);
        loginbutton.setOnClickListener(this);


    }

    /**
     *  Triggered on click of an element with the clickable property. on click of signup button, signs user up with specified info. on click of home button switches to homescreen.
     * @param v the button that was pressed
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signupButton) {
            String userName = String.valueOf(usernameInputBox.getText());
            String passwordInput = String.valueOf(passwordInputBox.getText());
            String passwordInput2 = String.valueOf(passwordInputBox2.getText());
            


            String fullName = String.valueOf(nameInputBox.getText());
            String firstName = "";
            String lastName = "";
            if(!(fullName.contains(" "))){
                errorMesaggeBox.setText("Please enter a first and last name");
            }
            if (fullName != null && fullName.contains(" ")) {
                firstName = fullName.substring(0, fullName.indexOf(" "));
                lastName = fullName.substring(fullName.indexOf(" ") + 1);
            } else {
                firstName = fullName != null ? fullName : "";
                lastName = "";
            }

            String email = String.valueOf(emailInputBox.getText());
            String phoneNumber = String.valueOf(phoneNumberInputBox.getText());
            String ssn = String.valueOf(ssnInputBox.getText());


            if(!(passwordInput.equals(passwordInput2))){
                errorMesaggeBox.setText("Passwords Do Not Match");
            } else if(userName.isEmpty()){
                errorMesaggeBox.setText("User Name Is Empty");
            } else if(passwordInput.isEmpty()){
                errorMesaggeBox.setText("Password is empty");
            } else if (!(ssn.matches("^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$"))){
                errorMesaggeBox.setText("Please enter a valid ssn");
            } else if (!(phoneNumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$"))) {
                errorMesaggeBox.setText("Please enter a valid phone number");
            }else if (!(email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,6}"))) {
                errorMesaggeBox.setText("Please enter a valid email");
            }else if(!(userName.matches("[a-zA-Z0-9]+"))){
                errorMesaggeBox.setText("Username may only contain letters and numbers");
            }

            else {
                try{
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("ssn", ssn);
                    jsonBody.put("firstName", firstName);
                    jsonBody.put("lastName", lastName);
                    jsonBody.put("userName", userName);
                    jsonBody.put("email", email);
                    jsonBody.put("phoneNumber", phoneNumber);
                    jsonBody.put("password", passwordInput);
                    sendUserPassAndUserToDB(jsonBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
        else if(id == R.id.loginButton){
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

        }
    }

    /**
     * Sends an http post request with the users info to the backend.
     * @param jsonBody to hold the users info that will be stored in the database
     */
    private void  sendUserPassAndUserToDB(JSONObject jsonBody) {

        final String mRequestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Account Created Successfully", response);
                        errorMesaggeBox.setText(response.toString());
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
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
                            Log.e("Account Creation Error", error.toString());
                            errorMesaggeBox.setText(error.toString());
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
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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
