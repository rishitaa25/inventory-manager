package com.example.androidexample.features.shipments.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.features.chats.ChatView;
import com.example.androidexample.features.chats.ChatsMain;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.shipments.services.ShipmentItemRequestMethods;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;
import com.example.androidexample.features.shipments.ui.adapters.ShipmentItemAdapter;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;

import java.util.ArrayList;
import java.util.List;

public class ShipmentViewActivity extends AppCompatActivity{

        // UI components
        private ImageView backButton, profile, deleteButton, editButton, searchArrow, menu, shipmentChat;
        private TextView shipmentItemStatusMsg, shipmentInfo, shipmentID;
        private EditText itemId;
        private List<ShipmentItemObject> shipmentItemList;
        private ListView shipmentItemListView;
        private ShipmentItemAdapter adapter;
        private boolean isDeleteConfirmed = false;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shipment_item_list);

            // Get shipment object and it's attributes
            ShipmentObject shipment = (ShipmentObject) getIntent().getSerializableExtra("shipment_object");
            int shipmentId = shipment.getShipmentId();
            String creationAdmin = shipment.getCreationAdmin();
            String deliveryDate = shipment.getDeliveryDate().toString();
            String arrived = shipment.getArrived().toString();
            String offloaded = shipment.getOffloaded().toString();


            // Initialize UI components
            shipmentItemStatusMsg = findViewById(R.id.shipment_item_status_msg);
            //profile = findViewById(R.id.home_icon);
            deleteButton = findViewById(R.id.delete_icon);
            editButton = findViewById(R.id.edit_icon);
            shipmentID = findViewById(R.id.shipment_title);
            shipmentItemListView = findViewById(R.id.shipment_item_list_view);
            shipmentInfo = findViewById(R.id.shipment_info);
            backButton = findViewById(R.id.back_icon);
            searchArrow = findViewById(R.id.search_icon);
            itemId = findViewById(R.id.shipment_item_id);
            menu = findViewById(R.id.menu_icon);
            shipmentChat = findViewById(R.id.chat_icon);

            // set up banner
            BannerHelper.setupBanner(this, profile, menu);


            //set up list
            shipmentItemList = new ArrayList<>();

            List<ShipmentItemObject> items = shipment.getItems();
            Log.d("SHIPMENT_ITEMS", items.toString());

            if (items != null) {
                shipmentItemList.addAll(items);
            }

            shipmentInfo.setText("Arrival Date: " + deliveryDate + "\n" +
                    "Arrived: " + arrived + "\n" +
                    "Offloaded: " + offloaded + "\n" +
                    "Created by: " + creationAdmin);

            adapter = new ShipmentItemAdapter(this, shipmentItemList);
            shipmentItemListView.setAdapter(adapter);

            shipmentID.setText("Shipment: " + shipmentId);


//            profile.setOnClickListener(view ->
//                    startActivity(new Intent(ShipmentViewActivity.this, HomeActivity.class)));

            shipmentChat.setOnClickListener(view -> {
                Intent intent = new Intent(ShipmentViewActivity.this, ChatView.class);
                intent.putExtra("room", "shipment_" + shipmentId);
                startActivity(intent);
            });

            backButton.setOnClickListener(view ->
                    startActivity(new Intent(ShipmentViewActivity.this, ShipmentListViewActivity.class)));


            editButton.setOnClickListener(view -> {
                Intent intent = new Intent(ShipmentViewActivity.this, shipmentEditActivity.class);
                intent.putExtra("shipment_object", shipment);
                startActivity(intent);
            });


            deleteButton.setOnClickListener(view -> {

                if (!isDeleteConfirmed) {
                    // First click → ask for confirmation
                    shipmentItemStatusMsg.setText("Are you sure you want to delete? Click delete again to confirm.");
                    isDeleteConfirmed = true;
                    return;
                }

                ShipmentRequestMethods.deleteShipmentRequest(this, shipmentId, new NetworkCallback<String>() {

                    @Override
                    public void onSuccess(String response)
                    {
                        shipmentItemStatusMsg.setText(response);
                        startActivity(new Intent(ShipmentViewActivity.this, ShipmentListViewActivity.class));
                    }

                    @Override
                    public void onError(String error)
                    {
                        shipmentItemStatusMsg.setText(error);
                    }
                });


            });

            searchArrow.setOnClickListener(view -> {
                String stringId = itemId.getText().toString();

                int id;
                try {
                    id = Integer.parseInt(stringId);
                } catch (NumberFormatException e) {
                    shipmentItemStatusMsg.setText("ID must be a number");
                    return;
                }

                if (stringId.isEmpty()) {
                    shipmentItemStatusMsg.setText("Complete all fields");
                    return;
                }

                shipmentItemStatusMsg.setText("Loading...");

                ShipmentItemRequestMethods.viewItemRequest(this, id, new NetworkCallback<ShipmentItemObject>() {
                    @Override
                    public void onSuccess(ShipmentItemObject response) {
                        shipmentItemStatusMsg.setText("Success!");
                        Intent intent = new Intent(ShipmentViewActivity.this, ShipmentItemViewActivity.class);
                        intent.putExtra("shipment_object", response);

                        startActivity(intent);
                    }

                    @Override
                    public void onError(String error) {
                        shipmentItemStatusMsg.setText(error);
                    }
                });

            });

            shipmentItemListView.setOnItemClickListener((parent, view, position, id) -> {
                        ShipmentItemObject clickedShipment = (ShipmentItemObject) parent.getItemAtPosition(position);
                        int shipmentItemId = clickedShipment.getSkuId();

                        ShipmentItemRequestMethods.viewItemRequest(this, shipmentItemId, new NetworkCallback<ShipmentItemObject>() {
                            @Override
                            public void onSuccess(ShipmentItemObject response) {
                                shipmentItemStatusMsg.setText("Success!");
                                Intent intent = new Intent(ShipmentViewActivity.this, ShipmentItemViewActivity.class);
                                intent.putExtra("shipment_object", response);

                                startActivity(intent);
                            }

                            @Override
                            public void onError(String error) {
                                shipmentItemStatusMsg.setText(error);
                            }
                        });
            });

//            shipmentItemListView.setOnItemClickListener((parent, view, position, id) ->{
//                ShipmentItemObject clickedItem = (ShipmentItemObject) parent.getItemAtPosition(position);
//                //int shipmentItemId = clickedItem.getSkuId();
//
//                Log.d("ITEM_OBJECT", String.valueOf(clickedItem));
//
//                Intent intent = new Intent(ShipmentViewActivity.this, ShipmentItemViewActivity.class);
//                intent.putExtra("shipment_object", clickedItem);
//
//                startActivity(intent);
//
//
//            });


        }
}
