package com.example.androidexample.features.shipments.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;
import com.example.androidexample.features.shipments.services.ShipmentItemRequestMethods;

public class ShipmentItemViewActivity extends AppCompatActivity {


    //UI components
    private TextView itemStatusMSG, itemTitle, itemName, shippingCompany, storageLocation, quantity, weight, shipmentId, storageType, itemInfo;
    private ImageView backButton, editButton, deleteButton, profile, menu;
    private boolean isDeleteConfirmed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_item_view);

        // Get shipment object and it's attributes
        ShipmentItemObject item = (ShipmentItemObject) getIntent().getSerializableExtra("shipment_object");
        int itemId = item.getSkuId();


        // Initialize UI components
        itemStatusMSG = findViewById(R.id.shipment_item_status_msg);
        itemTitle = findViewById(R.id.item_title);
        itemInfo = findViewById(R.id.shipment_info);
        backButton = findViewById(R.id.back_icon);
        deleteButton = findViewById(R.id.delete_icon);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        itemName = findViewById(R.id.detail_item_name);
        shippingCompany = findViewById(R.id.detail_shipping_co);
        quantity = findViewById(R.id.detail_quantity);
        weight = findViewById(R.id.detail_weight);
        shipmentId = findViewById(R.id.detail_shipment_id);
        storageType = findViewById(R.id.detail_storage_type);
        storageLocation = findViewById(R.id.detail_location);



        itemTitle.setText("Item: " + itemId);

        itemName.setText(item.getItemName());
        shippingCompany.setText(item.getShippingCompany());
        storageLocation.setText(item.getStorageLocation());
        quantity.setText(String.valueOf(item.getQuantity()));
        weight.setText(String.valueOf(item.getWeight()) + " lbs");
        shipmentId.setText("#" + item.getShipment().getShipmentId());
        storageType.setText(""+item.getStorageType());

//        itemInfo.setText("Item Name: " + item.getItemName() + "\n" + "Shipping Company: " + item.getShippingCompany() + "\n" +
//                "Storage Location: " + item.getStorageLocation() + "\n" + "Quantity: " + item.getQuantity() + "\n" +
//                "Weight: " + item.getWeight() + "\n" +
//                "Shipment ID: " + item.getShipment().getShipmentId() + "\n" + "Storage Type: " + item.getStorageType());



        deleteButton.setOnClickListener(view -> {


            if (!isDeleteConfirmed) {
                // First click → ask for confirmation
                itemStatusMSG.setText("Are you sure you want to delete? Click delete again to confirm.");
                isDeleteConfirmed = true;
                return;
            }

            ShipmentItemRequestMethods.deleteItemRequest(this, itemId, new NetworkCallback<String>() {

                @Override
                public void onSuccess(String response) {
                    itemStatusMSG.setText("Item Removed");
                    startActivity(new Intent(ShipmentItemViewActivity.this, ShipmentListViewActivity.class));
                }
                @Override
                public void onError(String error) {
                    itemStatusMSG.setText(error);
                }
            });

        });

        BannerHelper.setupBanner(this, profile, menu);

        backButton.setOnClickListener(view ->
                startActivity(new Intent(ShipmentItemViewActivity.this, ShipmentListViewActivity.class)));

    }
}
