package com.example.androidexample.features.shipments.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.androidexample.VolleySingleton;
import com.example.androidexample.features.NetworkCallback;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;
import com.example.androidexample.features.shipments.model.ShipmentObject;
import com.example.androidexample.features.user.data.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds the request methods relating to shipment items
 *
 * The class holds the base url for the server and each method is responsible for adding their endpoints to the url.
 * There are request methods for adding, deleting, viewing, and updating individual items relating to a shipment as
 * well as multiple items relating to a shipment. The methods use a string request to send the request to the server.
 *
 * @author Olivia Blais
 */
public class ShipmentItemRequestMethods {

    /**
     * The base url for the controller
     */
    private static final String BASE_URL =
            "http://coms-3090-013.class.las.iastate.edu:8080/item/";
            //"http://10.0.2.2:8080/";

    // --- SINGLE SHIPMENT ITEM REQUEST METHODS --- //

    /**
     * Makes a request to add an item to the specified shipment to the server
     */
    public static void addItemRequest(Context context, int id, String company, String date, NetworkCallback callback   )
    {
       // TODO: add item request (only if shipment hasn't left"
    }

    /**
     * Makes a request to delete an item from the specified shipment on the server
     */
    public static void deleteItemRequest(Context context, int id, NetworkCallback callback)
    {
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                BASE_URL + "delete/" + id,

                response -> {
                    Log.d("ITEM_RAW_RESPONSE", response);

                    if (response.equals("true")) {
                        callback.onSuccess("Item Removed");
                    } else {
                        callback.onError("Item Removal Failed.");
                    }
                },

                error -> {
                    Log.e("ITEM_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("ITEM_STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }
                    callback.onError("Item Removal failed.");
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
     * Makes a request to view an item from the specified shipment on the server /find-by/sku/{itemSKU}
     */
    public static void viewItemRequest(Context context, int id, NetworkCallback callback)
    {

        Log.d("VIEW_ITEM_REQUEST", "Preparing request...");

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + "find-by/sku/" + id,
                null,

                response -> {
                    Log.d("ITEM_RAW_RESPONSE", response.toString());

                    try {
                        String itemName = response.getString("itemName");
                        JSONObject companyObj = response.getJSONObject("shippingCompany");
                        String shippingCompany = companyObj.getString("shippingCompanyName");
                        JSONObject shipmentJson = response.getJSONObject("shipment");
                        ShipmentObject shipment = new ShipmentObject(
                                shipmentJson.getInt("shipmentId"),
                                LocalDate.parse(shipmentJson.getString("arrivalDate")),
                                shipmentJson.getBoolean("arrived"),
                                shipmentJson.getBoolean("offloaded")
                        );
                        String storageLocation = response.getString("storageLocation");
                        int quantity = response.getInt("quantity");
                        double weight = response.getDouble("weight");
                       // String arrivalDateString = response.getString("arrivalDate");
                       // LocalDate arrivalDate = LocalDate.parse(arrivalDateString);
                        String storageType = response.getString("storageType");


                        ShipmentItemObject item = new ShipmentItemObject( id, itemName, shippingCompany, shipment,
                                                                            storageLocation, quantity, weight, storageType);

                        callback.onSuccess(item);
                        Log.d("ITEM_SUCCESS", "Loading item Successful");

                    } catch (JSONException e) {
                        Log.e("SHIPMENT_PARSE_ERROR", "Response parsing failed", e);
                        callback.onError("Invalid server response");
                    }
                },

                error -> {
                    Log.e("ITEM_ERROR", error.toString());

                    if (error.networkResponse != null) {
                        Log.e("ITEM_STATUS_CODE",
                                String.valueOf(error.networkResponse.statusCode));
                    }

                    callback.onError("Loading itmem failed.");
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
     * Makes a request to update an item from the specified shipment on the server
     *
     * @param id
     * @param company
     * @param date
     */
    public static void updateItemRequest(int id, String company, String date)
    {
        // TODO: update item request
    }

    //--- MULTiPLE SHIPMENT ITEMS REQUEST METHODS --- //

    /**
     * Makes a request to add multiple items to the specified shipment to the server
     */
    public static void addItemsRequest()
    {
        // TODO: add items request
    }

    /**
     * Makes a request to delete multiple items from the specified shipment on the server
     */
    public static void deleteItemsRequest(Context context, int id, NetworkCallback callback)
    {
        // TODO: delete items request
    }

    /**
     * Makes a request to update multiple items from the specified shipment on the server
     */
    public static void viewItemsRequest(Context context, NetworkCallback callback) {

        // TODO: view items request

    }

}
