package com.example.androidexample.features.notifications.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.features.navigation.Services.BannerHelper;
import com.example.androidexample.features.notifications.model.NotificationObject;
import com.example.androidexample.features.notifications.model.Status;
import com.example.androidexample.features.notifications.ui.adapter.NotificationAdapter;
import com.example.androidexample.features.shipments.ui.ShipmentListViewActivity;
import com.example.androidexample.features.shipments.ui.ShipmentViewActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;
import com.example.androidexample.R;
import com.example.androidexample.features.user.ui.ApprovalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this page will show a history of notifications and depending on the type, you can filter which you look at
 */
public class NotificationActivity extends AppCompatActivity {

    private TextView notMsg;
    private static final String TAG = "NotiicationActivity";
    private static final String BASE_URL = "http://coms-3090-013.class.las.iastate.edu:8080/" + "admin" +  "/notifications/";
    private ImageView profile, menu, filterAll, filterPending, filterPersonal;

    private ListView listView;
    private NotificationAdapter adapter;
    private List<NotificationObject> notificationList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        /* initialize UI elements */
        //notMsg = findViewById(R.id.notification_banner);
        profile = findViewById(R.id.profile_icon);
        menu = findViewById(R.id.menu_icon);
        //test = findViewById(R.id.shipment);
        filterAll      = findViewById(R.id.filter_all_icon);
        filterPending  = findViewById(R.id.filter_pending_icon);
        filterPersonal = findViewById(R.id.filter_personal_icon);
        listView = findViewById(R.id.shipment_item_list_view);
        adapter = new NotificationAdapter(this, notificationList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NotificationObject item = (NotificationObject) parent.getItemAtPosition(position);

            Intent intent = new Intent(NotificationActivity.this, ApprovalActivity.class);

            intent.putExtra("notification_id", item.getNotificationId());

            startActivity(intent);
        });

        // notMsg.setText("");
        // notMsg.setBackgroundResource(0);

        BannerHelper.setupBanner(this, profile, menu);

        filterAll.setOnClickListener(v ->
                makeViewNotificationsRequest(BASE_URL + "all"));
        filterPending.setOnClickListener(v ->
                makeViewNotificationsRequest(BASE_URL + "pending"));
        filterPersonal.setOnClickListener(v ->
                makeViewNotificationsRequest(BASE_URL + SessionManager.getUserId()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "shipment_channel",
                    "Shipment Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    1001
            );
        }
    }

    // For receiving notifications
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            runOnUiThread(() -> {
                String str = intent.getStringExtra("test");
                notMsg.setText(message);
                notMsg.setBackgroundResource(R.drawable.notification_banner);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(() -> {
//                    notMsg.setText(str);
//                    notMsg.setBackgroundResource(0);
//                }, 2000);
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - registering receiver");
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop - unregistering receiver");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    private void makeViewNotificationsRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    notificationList.clear();
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
                            notificationList.add(n);
                        } catch (JSONException e) {
                            Log.e(TAG, "Parse error", e);
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e(TAG, "Fetch error: " + error.getMessage());
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