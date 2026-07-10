package com.example.androidexample.features.notifications.websocket;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidexample.R;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class NotificationWebsocketService extends Service {

    private final Map<String, WebSocketClient> webSockets = new HashMap<>();
    private final Map<String, Boolean> isConnected = new HashMap<>();

    public NotificationWebsocketService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            if ("CONNECT".equals(action)) {
                String key = intent.getStringExtra("key");
                String url = intent.getStringExtra("url");
                connectWebSocket(key, url);

            } else if ("DISCONNECT".equals(action)) {
                String key = intent.getStringExtra("key");
                disconnectWebSocket(key);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("SendWebSocketMessage"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (WebSocketClient client : webSockets.values()) {
            client.close();
        }

        webSockets.clear();
        isConnected.clear();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void connectWebSocket(String key, String url) {

        if (webSockets.containsKey(key)) {
            Boolean connected = isConnected.get(key);

            if (Boolean.TRUE.equals(connected)) {
                Log.w("WS_DUPLICATE", key + " already connected — skipping");
                return;
            } else {
                Log.w("WS_STALE", key + " exists but not connected — closing old socket");
                try {
                    webSockets.get(key).close();
                } catch (Exception e) {
                    Log.e("WS_CLEANUP_ERROR", key + " failed to close old socket", e);
                }
            }
        }

        final String userKey = key;

        try {
            URI serverUri = URI.create(url);

            WebSocketClient webSocketClient = new WebSocketClient(serverUri) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(key, "Connected");
                    Log.d("WS_DEBUG", "Admin WS open, key=" + key + " url=" + serverUri);
                    isConnected.put(key, true);
                }

                @Override
                public void onMessage(String message) {
//                    if (key == null || message == null) return;
//
//                    Intent intent = new Intent("WebSocketMessageReceived");
//                    intent.putExtra("key", key);
//                    intent.putExtra("message", message);
//
//                    LocalBroadcastManager.getInstance(getApplicationContext())
//                            .sendBroadcast(intent);
                    Log.d("WSService", "MESSAGE RECEIVED: " + message);

                    sendNotification(message);

                    if (userKey == null || message == null) return;

                    Intent intent = new Intent("WebSocketMessageReceived");
                    intent.putExtra("key", userKey);
                    intent.putExtra("message", message);

                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(key, "Closed");
                    isConnected.put(key, false);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d(key, "Error");
                    isConnected.put(key, false);
                }
            };

            Log.d("WS_STATE", "OPEN CONFIRMED " + key);

            webSockets.put(key, webSocketClient);
            webSocketClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String key = intent.getStringExtra("key");
            String message = intent.getStringExtra("message");

            if (key == null || message == null) return;

            WebSocketClient webSocket = webSockets.get(key);

            if (webSocket != null && Boolean.TRUE.equals(isConnected.get(key))) {
                webSocket.send(message);
            }
        }
    };

    private void disconnectWebSocket(String key) {
        if (webSockets.containsKey(key)) {
            webSockets.get(key).close();
            isConnected.put(key, false);
        }
    }

    private void sendNotification(String message) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "shipment_channel")
                        .setSmallIcon(R.drawable.ic_shipments)
                        .setContentTitle("New Notification : Inventory Manager")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManagerCompat manager =
                NotificationManagerCompat.from(this);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

}