package com.example.androidexample.features.shipments.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
//import com.example.androidexample.ListItemObject;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.data.ShipmentRepository;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.user.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentRequestMethods {

    /**
     * The base url for the controller
     */
    private static final String BASE_URL =
            "http://coms-3090-013.class.las.iastate.edu:8080/shipment/";
            //"http://10.0.2.2:8080/";

// --- SINGLE SHIPMENT REQUEST METHODS --- //

    /**
     * Makes a request to add a shipment to the specified shipment to the server
     *
     * @param context
     * @param id the id of the Admin who cretaed the shipment request
     * @param date
     * @param callback
     */
    public static void addShipmentRequest(Context context, int id,  LocalDate date, List<ShipmentItemObject> items, NetworkCallback callback)
    {

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("id", id);
            requestBody.put("requestedDeliveryDate", date.toString());

            JSONArray itemsArray = new JSONArray();

            for (ShipmentItemObject item : items) {
                JSONObject obj = new JSONObject();

                obj.put("itemName", item.getItemName());
                obj.put("storageLocation", item.getStorageLocation());
                obj.put("quantity", item.getQuantity());
                obj.put("weight", item.getWeight());
                obj.put("storageType", item.getStorageType());
                itemsArray.put(obj);
            }

            requestBody.put("items", itemsArray);
            Log.d("SHIPMENT_REQUEST_BODY", requestBody.toString());

        } catch (JSONException e) {
            callback.onError("Invalid request format");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "add",
                requestBody,


                response -> {
                    Log.d("ADD_SHIPMENT_RESPONSE", response.toString());
                    try {
                        int shipmentId = response.getInt("shipmentId");
                        callback.onSuccess("Shipment #" + shipmentId + " created successfully");
                    } catch (JSONException e) {
                        callback.onError("Failed to parse server response");
                    }
                },


                error -> {
                    Log.e("ADD_SHIPMENT_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }

                    callback.onError("Shipment creation failed");
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

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /**
     * Makes a request to delete a shipment from the specified shipment on the server
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void deleteShipmentRequest(Context context, int id, NetworkCallback callback)
    {
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                BASE_URL + id + "/delete",

                response -> {
                    Log.d("SHIPMENT_RAW_RESPONSE", response);

                    if (response.equals("true")) {
                        callback.onSuccess("Shipment Removed");
                    } else {
                        callback.onError("Shipment Removal Failed.");
                    }
                },

                error -> {
                    Log.e("SHIPMENT_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("SHIPMENT_STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }
                    callback.onError("Shipment Removal failed.");
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

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Makes a request to view a shipment from the specified shipment from the clientside shipment repository
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void viewShipmentViaId(Context context, int id, NetworkCallback callback) {
        List<ShipmentObject> shipments = ShipmentRepository.getShipments();

        if (shipments == null || shipments.isEmpty()) {
            callback.onError("No shipments loaded");
            return;
        }

        for (ShipmentObject shipment : shipments) {
            if (shipment.getShipmentId() == id) {
                callback.onSuccess(shipment);
                return;
            }
        }

        callback.onError("Shipment with ID " + id + " not found");
    }

    /**
     * Makes a request to view a shipment from the specified shipment on the server
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void viewShipmentViaIdRequest(Context context, int id, NetworkCallback callback) {

            Log.d("LOGIN", "Preparing request...");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    BASE_URL + "find-by/id/" + id,
                    null,

                    response -> {
                        Log.d("SHIPMENT_RAW_RESPONSE", response.toString());

                        try {
                            Integer shipmentId = response.getInt("shipmentId");
                            String deliveryDateStr = response.getString("arrivalDate");
                            LocalDate deliveryDate = LocalDate.parse(deliveryDateStr);
                            Boolean arrived = response.getBoolean("arrived");
                            Boolean offloaded = response.getBoolean("offloaded");
                            String creationAdmin = response.optString("creationAdmin");
                            JSONArray itemsArray = response.getJSONArray("items");

                            Log.d("SHIPMENT_ITEMS1", itemsArray.toString());


                            List<ShipmentItemObject> items = new ArrayList<>();

                            for (int j = 0; j < itemsArray.length(); j++) {

                                Log.d("SHIPMENT_ITEM_OBJECT", itemsArray.getJSONObject(j).toString());

                                JSONObject itemJson = itemsArray.getJSONObject(j);

                                Log.d("SHIPMENT_ITEM_OBJECT2", itemJson.toString());

                                ShipmentItemObject item = new ShipmentItemObject(   itemJson.getInt("skuId"),
                                        itemJson.getString("ItemName"),
                                        itemJson.getInt("quantity"),
                                        itemJson.optString("description", "N/A"));

                                Log.d("SHIPMENT_ITEM", item.toString());

                                items.add(item);
                            }

                            //Log.d("SHIPMENT_ITEMS2", items.toString());

                            ShipmentObject shipment = new ShipmentObject(shipmentId, deliveryDate, arrived, offloaded);

                            callback.onSuccess(shipment);
                            Log.d("SHIPMENT_SUCCESS", "Loading Shipment Successful");

                        } catch (JSONException e) {
                            Log.e("SHIPMENT_PARSE_ERROR", "Response parsing failed", e);
                            callback.onError("Invalid server response");
                        }
                    },

                    error -> {
                        Log.e("SHIPMENT_ERROR", error.toString());

                        if (error.networkResponse != null) {
                            Log.e("SCHEDULE_STATUS_CODE",
                                    String.valueOf(error.networkResponse.statusCode));
                        }

                        callback.onError("Loading Shipment failed.");
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

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        }

    /**
     * Makes a request to update a shipment from the specified shipment on the server
     *
     * @param context
     * @param id
     * @param arrived
     * @param date
     * @param callback
     */
    public static void updateShipmentRequest(Context context, int id, Boolean arrived, Boolean offloaded, LocalDate date, NetworkCallback callback)
    {

        Log.d("SHIPMENT", "Preparing request...");

        JSONObject requestBody;

        try {
            requestBody = new JSONObject();
            requestBody.put("shipmentId", id);
            requestBody.put("arrived", arrived);
            requestBody.put("offloaded", offloaded);
            requestBody.put("arrivalDate", date.toString());

            Log.d("SHIPMENT_REQUEST_BODY", requestBody.toString());

        } catch (JSONException e) {
            Log.e("SHIPMENT", "Invalid JSON", e);
            Toast.makeText(context, "Invalid JSON format", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                BASE_URL + "update",

                response -> {
                    Log.d("UPDATE_RAW_RESPONSE", response);

                    if (response.equals("true")) {
                        callback.onSuccess("Shipment Updated");
                    } else {
                        callback.onError("Shipment update failed");
                    }
                },

                error -> {
                    Log.e("SHIPMENT_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("SHIPMENT_STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }

                    callback.onError("Shipment update failed.");
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
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


//--- MULTIPLE SHIPMENT REQUEST METHODS ---

    /**
     * Makes a request to update multiple shipments from the specified shipment on the server
     */
    public static void viewShipmentsRequest(Context context, NetworkCallback callback)
    {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,  // HTTP request method (GET)
                BASE_URL + "find/all",      // URL of the JSON array API
                null,                // Pass null as the request body since it's a GET request

                // Response listener for handling successful response
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SHIPMENT_RAW_RESPONSE", response.toString());

                        List<ShipmentObject> shipmentList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                int shipmentId = jsonObject.getInt("shipmentId");
                                LocalDate deliveryDate = LocalDate.parse(jsonObject.getString("arrivalDate"));
                                boolean arrived = jsonObject.getBoolean("arrived");
                                boolean offloaded = jsonObject.getBoolean("offloaded");
                                JSONArray itemsArray = jsonObject.getJSONArray("items");
                                String creationAdmin = jsonObject.optString("creationAdmin");


                                Log.d("SHIPMENT_ITEMS1", itemsArray.toString());


                                List<ShipmentItemObject> items = new ArrayList<>();

                                for (int j = 0; j < itemsArray.length(); j++) {

                                    Log.d("SHIPMENT_ITEM_OBJECT", itemsArray.getJSONObject(j).toString());

                                    JSONObject itemJson = itemsArray.getJSONObject(j);

                                    Log.d("SHIPMENT_ITEM_OBJECT2", itemJson.toString());

                                    ShipmentItemObject item = new ShipmentItemObject(   itemJson.getInt("skuId"),
                                                                                        itemJson.getString("itemName"),
                                                                                        itemJson.getInt("quantity"),
                                                                                        itemJson.optString("shippingCompanyName", "N/A"));

                                    Log.d("SHIPMENT_ITEM", item.toString());

                                    items.add(item);
                                }

                                Log.d("SHIPMENT_ITEMS2", items.toString());

                                ShipmentObject shipment = new ShipmentObject(shipmentId, deliveryDate, arrived, offloaded, items, creationAdmin);
                                shipmentList.add(shipment);

                            } catch (JSONException e) {
                                Log.e("SHIPMENT_PARSE_ERROR", "Response parsing failed", e);
                            }
                        }

                        callback.onSuccess(shipmentList);
                        Log.d("SHIPMENT_SUCCESS", "Loading Shipments Successful");
                    }
                },
                // Error listener for handling request failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString()); // Log error details
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                Log.d("SHIPEMENTS_TOKEN", SessionManager.getToken());
                headers.put("Authorization", "Bearer " + SessionManager.getToken());
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            // Override getParams() if you need to send request parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1"); // Example parameter
//                params.put("param2", "value2"); // Example parameter
                return params;
            }
        };

        // Adding request to the Volley request queue for execution
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }

    /**
     * Makes a request to add multiple shipments to the server
     */
    public static void addShipmentsRequest()
    {
        // TODO: add shipments request
    }

    /**
     * Makes a request to delete multiple shipments to the server
     */
    public static void deleteAllShipmentsRequest()
    {
        // TODO: delete shipments request
    }

    /**
     * Makes a request to for a csv file of all shipments
     */
    public static void downloadShipmentsRequest(Context context, NetworkCallback callback)
    {
        Log.d("CSV", "Preparing request...");

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "http://coms-3090-013.class.las.iastate.edu:8080/export/shipments",

                response -> {
                    Log.d("CSV_RESPONSE", response);

                    callback.onSuccess(response);
                },

                error -> {
                    Log.e("CSV_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }

                    callback.onError("Failed to download CSV");
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
    public static void connectToShipmentChatroom(int shipmentId){








    }

}
