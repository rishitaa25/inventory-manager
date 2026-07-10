package com.example.androidexample.features.shipments.ui;

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

import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class shipmentEditActivity extends AppCompatActivity {
    //UI components
    private Button updateButton, cancelButton;
    private TextView shipmentStatusMsg, shipmentID;
    private EditText shipmentScheduledDate;
    private ImageView profile, menu;
    private boolean isArrived, isOffloaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_edit);

        // Intialize UI components
        shipmentStatusMsg = findViewById(R.id.shipment_status_msg);
        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);
        shipmentScheduledDate = findViewById(R.id.shipment_shipmentDate);
        shipmentID = findViewById(R.id.schedule_title);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);


        CheckBox arrivedCheck = findViewById(R.id.arrived_check);
        CheckBox offloadCheck = findViewById(R.id.offload_check);

        // Get shipment object and it's attributes from previous page
        ShipmentObject shipment = (ShipmentObject) getIntent().getSerializableExtra("shipment_object");
        int id = shipment.getShipmentId();

        shipmentID.setText("Update Shipment: " + id);
        isArrived = shipment.getArrived();
        isOffloaded = shipment.getOffloaded();


        // Set onclick for schedule button
        updateButton.setOnClickListener(view -> {

            if(arrivedCheck.isChecked())
                isArrived = true;

            if(offloadCheck.isChecked())
                isOffloaded = true;

            Log.d("ARRIVED", String.valueOf(isArrived));
            Log.d("OFFLOADED", String.valueOf(isOffloaded));

            DateTimeFormatter formatter = null;
            LocalDate scheduledDate;

            try {
                // in oder to use LocalDate, we need ensure api is current enough
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    scheduledDate = LocalDate.parse(shipmentScheduledDate.getText().toString(), formatter);
                } else {
                    scheduledDate = null;
                }
            } catch (Exception e) {
                Log.e("DATE_PARSE_ERROR", "Invalid date format", e);
                Toast.makeText(this, "Invalid date format, you may need to add a zero in front of a single digit", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Invalid date format. Make sure it is MM-dd-yyyy");
                scheduledDate = null;
                return;
            }

            if ( scheduledDate == null) {
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Complete all fields");
                return;
            } else {
                ShipmentRequestMethods.updateShipmentRequest(this, id, isArrived, isOffloaded, scheduledDate, new NetworkCallback<String>(){
                    @Override
                    public void onSuccess(String message) {
                        shipmentStatusMsg.setText(message);
                        startActivity(new Intent(shipmentEditActivity.this, ShipmentListViewActivity.class));
                    }
                    @Override
                    public void onError(String message) {
                        shipmentStatusMsg.setText(message);
                    }
                });
            }
        });

        BannerHelper.setupBanner(this, profile, menu);

        // Set onclick for cancel button
        cancelButton.setOnClickListener(view ->
                startActivity(new Intent(shipmentEditActivity.this, ShipmentListViewActivity.class)));
    }
}


