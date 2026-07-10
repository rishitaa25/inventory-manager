package com.example.androidexample.features.shipments.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidexample.R;
import com.example.androidexample.features.shipments.model.ShipmentObject;

import java.util.List;
public class ShipmentAdapter extends ArrayAdapter<ShipmentObject> {


        public ShipmentAdapter(Context context, List<ShipmentObject> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ShipmentObject shipment = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_shipment, parent, false);
            }

            // Lookup view for data population
            TextView shipmentID = convertView.findViewById(R.id.shipment_ID);
            TextView  arrivalStatus = convertView.findViewById(R.id.shipment_status);
            TextView  offloadStatus = convertView.findViewById(R.id.offload_status);
            TextView  shipmentDate = convertView.findViewById(R.id.shipment_date);



            // Populate the data into the template view using the data object
            shipmentID.setText(String.valueOf("Shipment#: " + shipment.getShipmentId()));
            arrivalStatus.setText(shipment.getArrived() ? "Arrived" : "Not Arrived");
            offloadStatus.setText(shipment.getOffloaded() ? "Offloaded" : "Not Offloaded");
            shipmentDate.setText("Delivery: " + shipment.getDeliveryDate().toString());




            // Return the completed view to render on screen
            return convertView;
        }
    }


