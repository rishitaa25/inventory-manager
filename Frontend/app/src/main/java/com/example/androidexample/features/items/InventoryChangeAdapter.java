package com.example.androidexample.features.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidexample.R;

import java.util.List;


public class InventoryChangeAdapter extends ArrayAdapter<InventoryChangeObject> {
    /**
     * creates an adapter to manage the assignment of the xml fields for the array.
     * @param context for the screen that this is created on
     * @param items list of inventory change objects
     */
        public InventoryChangeAdapter(Context context, List<InventoryChangeObject> items) {
            super(context, 0, items);
        }

    /**
     * assigns the values of the designated view with the corresponding inventoryChangeObject value
     * @param position of the array in the view
     * @param convertView view that the values are assigned to
     * @param parent parent of the view
     * @return the view with populated fields
     */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            InventoryChangeObject item = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.change_item, parent, false);
            }

            // Lookup view for data population
            TextView changeId = convertView.findViewById(R.id.changeId);
            TextView skuNumber = convertView.findViewById(R.id.skuNumber);
            TextView amountOfItem = convertView.findViewById(R.id.amountOfItem);
            TextView employeeId = convertView.findViewById(R.id.employeeId);

            // Populate the data into the template view using the data object
            changeId.setText(String.valueOf("Change ID: " + item.getChangeId()));
            skuNumber.setText(String.valueOf("Sku Number: " + item.getSkuNumber()));
            amountOfItem.setText(String.valueOf("Item amount:" + item.getAmountOfItem()));
            employeeId.setText(String.valueOf("employee Id:" + item.getEmployeeId()));

            // Return the completed view to render on screen
            return convertView;
        }
    }

