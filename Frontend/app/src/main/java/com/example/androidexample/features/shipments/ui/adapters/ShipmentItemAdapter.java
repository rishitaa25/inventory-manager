package com.example.androidexample.features.shipments.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidexample.R;
import com.example.androidexample.features.shipments.model.ShipmentItemObject;
import com.example.androidexample.features.shipments.model.ShipmentObject;

import java.util.List;

public class ShipmentItemAdapter extends ArrayAdapter<ShipmentItemObject> {


    public ShipmentItemAdapter(Context context, List<ShipmentItemObject> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShipmentItemObject item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_shipment_items, parent, false);
        }

        // Lookup view for data population
        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemId = convertView.findViewById(R.id.item_sku);
        TextView itemlocation = convertView.findViewById(R.id.shippingLocation);


        // Populate the data into the template view using the data object
        itemName.setText(String.valueOf(item.getItemName()));
        itemId.setText("ID#"+String.valueOf(item.getSkuId()));
        itemlocation.setText(String.valueOf(item.getStorageLocation()));


        // Return the completed view to render on screen
        return convertView;
    }
}
