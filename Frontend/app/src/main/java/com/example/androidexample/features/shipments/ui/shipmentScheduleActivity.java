package com.example.androidexample.features.shipments.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.enums.StorageType;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class shipmentScheduleActivity extends AppCompatActivity {

    //UI components
    private Button scheduleButton, cancelButton;
    private ImageView addItemButton, profile, menu;
    private TextView shipmentStatusMsg;
    private EditText shipmentScheduledDate, itemNameText, storageLocationText, quantityText, weightText, storageTypeText, adminIdText;
    private boolean isScheduleConfirmed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_sched);

        // Intialize UI components
        shipmentStatusMsg = findViewById(R.id.shipment_status_msg);
        scheduleButton = findViewById(R.id.schedule_button);
        cancelButton = findViewById(R.id.cancel_button);
        shipmentScheduledDate = findViewById(R.id.shipment_shipmentDate);
        adminIdText = findViewById(R.id.shipment_admin);
        addItemButton = findViewById(R.id.addItemButton);
        itemNameText = findViewById(R.id.itemNameInput);
        storageLocationText = findViewById(R.id.storageLocationInput);
        quantityText = findViewById(R.id.quantityInput);
        weightText = findViewById(R.id.weightInput);
        storageTypeText = findViewById(R.id.storageType);
        profile = findViewById(R.id.profile_icon);

        BannerHelper.setupBanner(this, profile, menu);

        // Get Shipment Items from user

        List<ShipmentItemObject> items = new ArrayList<>();

        addItemButton.setOnClickListener(view -> {

            String itemName = itemNameText.getText().toString().trim();
            String storageLocation = storageLocationText.getText().toString().trim();
            String quantity = quantityText.getText().toString().trim();
            String weight = weightText.getText().toString().trim();
            String storageTypeString = storageTypeText.getText().toString().trim();

            if(itemName.isEmpty() || storageLocation.isEmpty() || quantity.isEmpty() || weight.isEmpty() || storageTypeString.isEmpty()){
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Complete all fields");
                return;
            } else {

                Log.d("ITEM", itemName + " " + storageLocation + " " + quantity + " " + weight + " " + storageTypeString);

                try{
                    StorageType storageType = StorageType.valueOf(storageTypeString);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, "Invalid storage type", Toast.LENGTH_SHORT).show();
                    shipmentStatusMsg.setText("Invalid storage type. Chose from DRY, REFRIGERATED, or FROZEN");
                    return;
                }

                try {
                    int quantityInt = Integer.parseInt(quantity);
                    int weightInt = Integer.parseInt(weight);

                    // add item to items list
                    items.add(new ShipmentItemObject(itemName, storageLocation,  quantityInt, weightInt, storageTypeString));

                    // clear text fields
                    itemNameText.setText("");
                    storageLocationText.setText("");
                    quantityText.setText("");
                    weightText.setText("");
                    storageTypeText.setText("");
                    shipmentStatusMsg.setText("Item Added");
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Quantity and Weight must be valid numbers", Toast.LENGTH_SHORT).show();
                    shipmentStatusMsg.setText("Invalid quantity or weight");
                }
            }

        });


        // Set onclick for schedule button
        scheduleButton.setOnClickListener(view -> {

            if (!isScheduleConfirmed)
            {
                // First click → ask for confirmation
                shipmentStatusMsg.setText("Are all items added? Click schedule again to confirm.");
                isScheduleConfirmed = true;
                return;
            }

            String adminIdString = adminIdText.getText().toString().trim();
            if (adminIdString.isEmpty()) {
                Toast.makeText(this, "Enter Admin ID", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Enter Admin ID");
                return;
            }

            int adminId;
            try {
                adminId = Integer.parseInt(adminIdString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Admin ID must be a number", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Invalid Admin ID");
                return;
            }

            // Get information from text fields
            DateTimeFormatter formatter = null;
            LocalDate scheduledDate;

            String dateString = shipmentScheduledDate.getText().toString().trim();
            if (dateString.isEmpty()) {
                Toast.makeText(this, "Enter a date", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Enter a date");
                return;
            }

            try {
                // in oder to use LocalDate, we need ensure api is currrent enoguh
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    scheduledDate = LocalDate.parse(dateString, formatter);
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

            if (scheduledDate == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return;
            }

            if (items.isEmpty()) {
                Toast.makeText(this, "Add at least one item", Toast.LENGTH_SHORT).show();
                shipmentStatusMsg.setText("Add at least one item before scheduling");
                return;
            }

            Log.d("ITEMS", items.toString());
            Log.d("DATE", scheduledDate != null ? scheduledDate.toString() : "null");
            Log.d("ADMIN", adminIdString);
            ShipmentRequestMethods.addShipmentRequest(this, adminId, scheduledDate, items, new NetworkCallback<String>() {
                @Override
                public void onSuccess(String message) {
                    shipmentStatusMsg.setText(message);
                    // notify via WebSocket after successful schedule

                    startActivity(new Intent(shipmentScheduleActivity.this, ShipmentListViewActivity.class));
                }
                @Override
                public void onError(String message) {
                    shipmentStatusMsg.setText(message);
                }
            });
        });

        // Set onclick for cancel button
        cancelButton.setOnClickListener(view ->
                startActivity(new Intent(shipmentScheduleActivity.this, ShipmentListViewActivity.class)));


    }


}
