package com.example.androidexample.features.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.R;
import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.notifications.websocket.NotificationWebsocketService;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Login activity for the application.
 *
 * This activity allows users to log in to the application by prompting the user for
 * their username and password. If the login is successful, a session is created using
 * the SessionManager and the user is redirected to the main activity. If the login fails,
 * an error message is displayed.
 *
 * This class uses HTTP POST requests to communicate with the backend server.
 *
 * @author Olivia Blais
 */
public class LoginActivity extends AppCompatActivity {

    private TextView loginMessage;
    private EditText usernameEnter, passwordEnter;
    private Button loginButton, signupButton;
    private Spinner accountTypeSpinner;
    private String accountType = "";

    private static final String BASE_URL = "http://coms-3090-013.class.las.iastate.edu:8080/";
    private static final String ADMIN_ENDPOINT = "admin/login";
    private static final String EMPLOYEE_ENDPOINT = "employee/login";
    private static final String DRIVER_ENDPOINT = "driver/login";

    /**
     * Initializes the login activity and handles user input.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginMessage = findViewById(R.id.login_mesg);
        passwordEnter = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        usernameEnter = findViewById(R.id.login_username);
        accountTypeSpinner = findViewById(R.id.login_account_type_spinner);

        setupAccountTypeSpinner();

        loginButton.setOnClickListener(view -> {
            String username = usernameEnter.getText().toString();
            String password = passwordEnter.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                loginMessage.setText("Please enter username and password");
                return;
            }

            if (accountType.isEmpty()) {
                loginMessage.setText("Please select an account type");
                return;
            }

            try {
                if (accountType.equals("Admin"))
                    makeLoginRequest(username, password, BASE_URL + ADMIN_ENDPOINT);
                else if (accountType.equals("Employee"))
                    makeLoginRequest(username, password, BASE_URL + EMPLOYEE_ENDPOINT);
                else if (accountType.equals("Driver"))
                    makeLoginRequest(username, password, BASE_URL + DRIVER_ENDPOINT);
            } catch (JSONException e) {
                loginMessage.setText("Invalid server response");
                throw new RuntimeException(e);
            }
        });

        signupButton.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    /**
     * Sets up the account type spinner with options for admin, employee, and driver.
     * Updates the accountType field when the user makes a selection.
     */
    private void setupAccountTypeSpinner() {
        String[] accountTypes = {"Select Account Type", "Admin", "Employee", "Driver"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                accountTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);

        accountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    accountType = "";
                } else {
                    accountType = selected;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                accountType = "";
            }
        });
    }

    /**
     * Sends a POST request to the server to attempt a login.
     *
     * The username and password are sent as a JSON body.
     *
     * If the login is successful:
     * - user information is extracted from the response
     * - it will store the session in SessionManager
     * - it will display the success message.
     * - The user is then redirected to the main activity.
     *
     * Otherwise, an error message is displayed and logged.
     *
     * @param username    the username entered by the user
     * @param password    the password entered by the user
     * @param SELECTED_URL the endpoint URL based on account type
     */
    public void makeLoginRequest(String username, String password, String SELECTED_URL) throws JSONException {

        Log.d("LOGIN", "Preparing request...");
        Log.d("LOGIN_USERNAME", username);
        Log.d("LOGIN_PASSWORD", password);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", username);
        jsonBody.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SELECTED_URL,
                jsonBody,

                response -> {
                    Log.d("LOGIN_RAW_RESPONSE", response.toString());

                    try {
                        SessionManager.saveSession(response.getString("token"),
                                response.getInt("employeeId"), AccessLevel.valueOf(response.getString("accessLevel")), username);

                        String message = response.getString("message");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String accessLevelString = response.getString("accessLevel");
                        AccessLevel accessLevel = AccessLevel.valueOf(accessLevelString);
                        String token = response.getString("token");

                        loginMessage.setText(message + "\nWelcome " + firstName + " " + lastName);
                        Log.d("LOGIN_SUCCESS", "Login successful");
                        Log.d("LOGIN_TOKEN", token);
                        Log.d("LOGIN_FIRST_NAME", firstName);
                        Log.d("LOGIN_LAST_NAME", lastName);
                        Log.d("LOGIN_ACCESS_LEVEL", accessLevel.toString());

                        // disconnect old sockets first
                        String[] wsKeys = {"shipments", "admin", "employee"};
                        for (String key : wsKeys) {
                            Intent disconnectIntent = new Intent(this, NotificationWebsocketService.class);
                            disconnectIntent.setAction("DISCONNECT");
                            disconnectIntent.putExtra("key", key);
                            startService(disconnectIntent);
                        }

                        // reconnect after short delay
                        new Handler().postDelayed(() -> {
                            String[] keys = {"shipments", "admin", "employee"};
                            String[] urls = {
                                    "ws://coms-3090-013.class.las.iastate.edu:8080/shipments/" + username,
                                    "ws://coms-3090-013.class.las.iastate.edu:8080/admin-notifications/" + username,
                                    "ws://coms-3090-013.class.las.iastate.edu:8080/employee-notifications/" + username
                            };
                            for (int i = 0; i < keys.length; i++) {
                                Intent wsIntent = new Intent(this, NotificationWebsocketService.class);
                                wsIntent.setAction("CONNECT");
                                wsIntent.putExtra("key", keys[i]);
                                wsIntent.putExtra("url", urls[i]);
                                startService(wsIntent);
                            }
                        }, 1000);

//                        Intent intent = new Intent(this, NotificationWebsocketService.class);
//                        intent.setAction("CONNECT");
//                        intent.putExtra("key", username);
//                        intent.putExtra("url", "ws://coms-3090-013.class.las.iastate.edu:8080/shipments/" + username);
//                        startService(intent);

                        new Handler().postDelayed(() ->
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class)), 1000);

                    } catch (JSONException e) {
                        Log.e("LOGIN_PARSE_ERROR", "Response parsing failed", e);
                        loginMessage.setText("Login failed. Check username/password.");
                    }
                },

                error -> {
                    Log.e("LOGIN_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("LOGIN_STATUS_CODE", String.valueOf(error.networkResponse.statusCode));
                    }

                    loginMessage.setText("Login failed. Check username/password.");
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}

