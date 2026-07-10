package com.example.androidexample.features.shipments.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.data.ShipmentRepository;
import com.example.androidexample.features.shipments.ui.adapters.ShipmentAdapter;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShipmentListViewActivity extends AppCompatActivity {

    // UI components
    private ImageView searchArrow, profile, addButton, menu, downloadShipments, shipmentChat;
    private TextView shipmentStatusMsg, addText;
    private EditText shipmentID;
    private List<ShipmentObject> shipmentList;
    private ListView shipmentListView;
    private ShipmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipments_view);

        // Initialize UI components
        shipmentStatusMsg = findViewById(R.id.shipment_status_msg);
        searchArrow = findViewById(R.id.search_icon);
        profile = findViewById(R.id.profile_icon);
        shipmentID = findViewById(R.id.shipment_id);
        shipmentListView = findViewById(R.id.shipment_list_view);
        addButton = findViewById(R.id.add_icon);
        menu = findViewById(R.id.menu_icon);
        downloadShipments = findViewById(R.id.download_icon);
        addText = findViewById(R.id.add_text);
        shipmentChat = findViewById(R.id.chat_icon);


        shipmentList = new ArrayList<>();
        adapter = new ShipmentAdapter(this, shipmentList);
        shipmentListView.setAdapter(adapter);


        // ensure only admin level access can create shipments
        if (SessionManager.getAccessLevel() != AccessLevel.ADMIN) {
            addButton.setVisibility(ImageView.GONE);
            addText.setVisibility(TextView.GONE);
            //downloadShipments
        }




        shipmentStatusMsg.setText("Loading...");

        ShipmentRequestMethods.viewShipmentsRequest(this, new NetworkCallback<List<ShipmentObject>>() {
            @Override
            public void onSuccess(List<ShipmentObject> response) {
                ShipmentRepository.shipments = response;
                shipmentStatusMsg.setText("");
                shipmentList.clear();
                shipmentList.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                shipmentStatusMsg.setText(error);
            }
        });

        downloadShipments.setOnClickListener(view -> {
            shipmentStatusMsg.setText("Downloading...");
            ShipmentRequestMethods.downloadShipmentsRequest(this, new NetworkCallback() {
                @Override
                public void onSuccess(Object result) {
                    String csv = (String) result;

                    saveCsvToFile(ShipmentListViewActivity.this, csv);

                    shipmentStatusMsg.setText("CSV saved!");
                }

                @Override
                public void onError(String error) {
                    shipmentStatusMsg.setText(error);
                }
            });
        });

        searchArrow.setOnClickListener(view -> {
            String stringId = shipmentID.getText().toString();

            int id;
            try {
                id = Integer.parseInt(stringId);
            } catch (NumberFormatException e) {
                shipmentStatusMsg.setText("ID must be a number");
                return;
            }

            if (stringId.isEmpty()) {
                shipmentStatusMsg.setText("Complete all fields");
                return;
            }

            shipmentStatusMsg.setText("Loading...");

            ShipmentRequestMethods.viewShipmentViaId(this, id, new NetworkCallback<ShipmentObject>() {
               @Override
               public void onSuccess(ShipmentObject response) {
                   shipmentStatusMsg.setText("Success!");
                   Intent intent = new Intent(ShipmentListViewActivity.this, ShipmentViewActivity.class);
                   intent.putExtra("shipment_object", response);
                   startActivity(intent);

               }
               @Override
               public void onError(String error) {
                   shipmentStatusMsg.setText(error);
               }
            });

        });

        shipmentListView.setOnItemClickListener((parent, view, position, id) -> {
            ShipmentObject clickedShipment = (ShipmentObject) parent.getItemAtPosition(position);

            Intent intent = new Intent(ShipmentListViewActivity.this, ShipmentViewActivity.class);
            intent.putExtra("shipment_object", clickedShipment);

            startActivity(intent);

        });

        // set up banner
        BannerHelper.setupBanner(this, profile, menu);

        // admin access may add shipments
        if (SessionManager.getAccessLevel() == AccessLevel.ADMIN) {
            addButton.setOnClickListener(view ->
                    startActivity(new Intent(ShipmentListViewActivity.this, shipmentScheduleActivity.class)));
        }
    }
    private static void saveCsvToFile(Context context, String csvData) {
        try {
            String fileName = "shipments.csv";

            File file = new File(context.getExternalFilesDir(null), fileName);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(csvData.getBytes());
            fos.close();

            Log.d("CSV_SAVE", "Saved at: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}