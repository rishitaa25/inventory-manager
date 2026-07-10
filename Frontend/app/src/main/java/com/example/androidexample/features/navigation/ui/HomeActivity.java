package com.example.androidexample.features.navigation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.R;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.chats.ChatsMain;
import com.example.androidexample.features.items.ItemManagementNavPage;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.notifications.model.NotificationObject;
import com.example.androidexample.features.notifications.model.Status;
import com.example.androidexample.features.notifications.ui.NotificationActivity;
import com.example.androidexample.features.notifications.ui.adapter.NotificationAdapter;
import com.example.androidexample.features.shifts.ShiftNav;
import com.example.androidexample.features.shipments.data.ShipmentRepository;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.shipments.services.ShipmentRequestMethods;
import com.example.androidexample.features.shipments.ui.ShipmentListViewActivity;
import com.example.androidexample.features.shipments.ui.adapters.ShipmentAdapter;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    // Ui components
    private ImageView shipments, notifications, chat, inventory, profile, menu, shifts;

    // ADDED: home lists
    private ListView homeShipmentsList;
    private ListView homeNotificationsList;

    private ShipmentAdapter shipmentAdapter;
    private NotificationAdapter notificationAdapter;

    private List<ShipmentObject> shipmentPreview = new ArrayList<>();
    private List<NotificationObject> notificationPreview = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize UI components
        shipments = findViewById(R.id.shipment);
        notifications = findViewById(R.id.notifications);
        chat = findViewById(R.id.chat);
        inventory = findViewById(R.id.inventory);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        shifts = findViewById(R.id.shifts);

        // ADDED: home list views
        homeShipmentsList = findViewById(R.id.home_shipments_list);
        homeNotificationsList = findViewById(R.id.home_notifications_list);

        BannerHelper.setupBanner(this, profile, menu);

        // ADDED: shipments preview from repository
        if (ShipmentRepository.shipments != null) {
            int limit = Math.min(5, ShipmentRepository.shipments.size());
            shipmentPreview.addAll(ShipmentRepository.shipments.subList(0, limit));
        }

        shipmentAdapter = new ShipmentAdapter(this, shipmentPreview);
        homeShipmentsList.setAdapter(shipmentAdapter);

        // ADDED: notifications placeholder (replace when you have real source)
        notificationAdapter = new NotificationAdapter(this, notificationPreview);
        homeNotificationsList.setAdapter(notificationAdapter);

        // Set click listeners for UI components
        shipments.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ShipmentListViewActivity.class));
        });

        notifications.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
        });

        chat.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ChatsMain.class));
        });

        inventory.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ItemManagementNavPage.class));
        });

        shifts.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ShiftNav.class));
        });

        makeViewNotificationsRequest("http://coms-3090-013.class.las.iastate.edu:8080/admin/notifications/all");

        ShipmentRequestMethods.viewShipmentsRequest(this, new NetworkCallback<List<ShipmentObject>>() {
            @Override
            public void onSuccess(List<ShipmentObject> response) {

                ShipmentRepository.shipments = response;

                shipmentPreview.clear();

                int limit = Math.min(5, response.size());
                shipmentPreview.addAll(response.subList(0, limit));

                shipmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ADDED: refresh when returning to home
    @Override
    protected void onResume() {
        super.onResume();

        if (ShipmentRepository.shipments != null) {
            shipmentPreview.clear();
            int limit = Math.min(5, ShipmentRepository.shipments.size());
            shipmentPreview.addAll(ShipmentRepository.shipments.subList(0, limit));
            shipmentAdapter.notifyDataSetChanged();
        }
    }



    private void makeViewNotificationsRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    notificationPreview.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String statusStr = "PENDING";
                            if (!obj.isNull("status")) {
                                statusStr = obj.getString("status");
                            }

                            String recipientStr = "ADMIN";
                            if (!obj.isNull("recipient")) {
                                recipientStr = obj.getString("recipient");
                            }

                            NotificationObject n = new NotificationObject(
                                    obj.getInt("id"),
                                    AccessLevel.valueOf(recipientStr),
                                    obj.optString("message", ""),
                                    LocalDate.parse(obj.optString("date", LocalDate.now().toString())),
                                    Status.valueOf(statusStr),
                                    obj.optInt("requestedByEmployeeId", 0),
                                    obj.optString("requesterType", ""),
                                    obj.optString("firstName", ""),
                                    obj.optString("lastName", ""),
                                    obj.optString("userName", ""),
                                    obj.optString("email", ""),
                                    obj.optString("phoneNumber", ""),
                                    obj.optString("driver", "")
                            );
                            notificationPreview.add(n);
                        } catch (JSONException e) {
                            Log.e("TAG", "Parse error", e);
                        }
                    }
                    runOnUiThread(() -> notificationAdapter.notifyDataSetChanged());
                },
                error -> {
                    Log.e("TAG", "Fetch error: " + error.getMessage());
                    Toast.makeText(this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                //Log.d("LOGOUT_TOKEN", SessionManager.getToken());
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
//                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }
}