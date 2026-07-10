package com.example.androidexample.features.user.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.navigation.ui.HomeActivity;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;
import com.example.androidexample.features.shipments.ui.ShipmentListViewActivity;
import com.example.androidexample.features.shipments.ui.shipmentEditActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.model.Employee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ApprovalActivity extends AppCompatActivity {
    private Button updateButton, cancelButton;
    private TextView shipmentStatusMsg, shipmentID;
    private EditText shipmentScheduledDate;
    private ImageView profile, menu;
    private boolean isArrived, isOffloaded;
    private int notificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approval);

        // Intialize UI components
        shipmentStatusMsg = findViewById(R.id.shipment_status_msg);
        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);


        CheckBox approved = findViewById(R.id.arrived_check);

        // Get Emplyee object and it's attributes from previous page
        notificationId = getIntent().getIntExtra("notification_id", -1);



        // Set onclick for schedule button
        updateButton.setOnClickListener(view -> {

            if(approved.isChecked())
                isArrived = true;

                makeApprovalRequest(notificationId);
                startActivity(new Intent(ApprovalActivity.this, HomeActivity.class));

        });

        BannerHelper.setupBanner(this, profile, menu);

        // Set onclick for cancel button
        cancelButton.setOnClickListener(view ->
                startActivity(new Intent(ApprovalActivity.this, HomeActivity.class)));
    }


    private void makeApprovalRequest(int id){
        String url = "http://coms-3090-013.class.las.iastate.edu:8080/admin/notifications/"
                + id + "/approve";

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                response -> {
                    Log.d("APPROVAL_RESPONSE", response);
                    shipmentStatusMsg.setText("Approved successfully.");
                    startActivity(new Intent(ApprovalActivity.this, HomeActivity.class));
                },
                error -> {
                    Log.e("APPROVAL_ERROR", String.valueOf(error));
                    shipmentStatusMsg.setText("Approval failed.");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}

