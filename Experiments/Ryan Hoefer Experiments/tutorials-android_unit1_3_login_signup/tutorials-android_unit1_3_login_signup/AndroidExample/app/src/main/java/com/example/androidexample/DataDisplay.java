package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class DataDisplay extends AppCompatActivity {


    private Button newBoxButton;
    private Spinner dropDownMenu;
    private ScrollView dataScrollView;
    private LinearLayout dropdownContainer;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_display);
        counter = 0;// counter for data ints
        /* Initialize UI Elements*/
        dataScrollView = findViewById(R.id.data_scroll_view);
        dropdownContainer = findViewById(R.id.dropdown_container);
        newBoxButton = findViewById(R.id.new_box_button);
        dropDownMenu = findViewById(R.id.data_dropdown);

       addNewSpinner();// add first data dropdown
        //add new spinner on click
        newBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSpinner();

            }
        });

    }

    private void addNewSpinner(){

        Spinner newSpinner = new Spinner(this, Spinner.MODE_DROPDOWN);

        String[] baseList = getResources().getStringArray(R.array.data_array);//Put the default list in strings.xml into an array

        ArrayList<String> DataDropdownOptions = new ArrayList<>(Arrays.asList(baseList));//put the previous array into an array list
        DataDropdownOptions.clear();//clear the list just incase
        //add the three data vals
        DataDropdownOptions.add("Data val " + ++counter);
        DataDropdownOptions.add("Data val " + ++counter);
        DataDropdownOptions.add("Data val " + ++counter);

        //create adapter to link the dropdown view with the arraylist using a simple spinner when it is collapsed
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataDropdownOptions);
       //set the template it uses when it is expanded
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the new adapter within the spinner
        newSpinner.setAdapter(adapter);
        //add the spinner to the layout container that will house all of the dropdown spinners
        dropdownContainer.addView(newSpinner);









    }
}
