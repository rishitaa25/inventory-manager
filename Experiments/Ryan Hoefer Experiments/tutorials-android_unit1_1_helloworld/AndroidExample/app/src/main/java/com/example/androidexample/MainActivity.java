package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.core.content.ContextCompat;

/*

1. To run this project, open the directory "Android Example", otherwise it may not recognize the file structure properly

2. Ensure you are using a compatible version of gradle, to do so you need to check 2 files.

    AndroidExample/Gradle Scripts/build.gradle
    Here, you will have this block of code. Ensure it is set to a compatible version,
    in this case 8.12.2 should be sufficient:
        plugins {
            id 'com.android.application' version '8.12.2' apply false
        }

    Gradle Scripts/gradle-wrapper.properties

3. This file is what actually determines the Gradle version used, 8.13 should be sufficient.
    "distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip" ---Edit the version if needed

4. You might be instructed by the plugin manager to upgrade plugins, accept it and you may execute the default selected options.

5. Press "Sync project with gradle files" located at the top right of Android Studio,
   once this is complete you will be able to run the app

   This version is compatible with both JDK 17 and 21. The Java version you want to use can be
   altered in Android Studio->Settings->Build, Execution, Deployment->Build Tools->Gradle

 */

public class MainActivity extends AppCompatActivity {

    private TextView HelloWorldText;   // define message textview variable
    private TextView QuestionTextBox;
    private TextView ReplyTextBox;
    private TextInputEditText inputBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /*Initialize then change elements for the hello world message*/
        HelloWorldText = findViewById(R.id.main_msg_txt);//Initialize hello world message
        HelloWorldText.setTextColor(ContextCompat.getColor(this, R.color.blue));
        HelloWorldText.setText("Hello World");
        HelloWorldText.setLetterSpacing(0.5f);
        HelloWorldText.setRotationX(55);

        QuestionTextBox = findViewById(R.id.second_msg_txt); //Initialize question asked message
        QuestionTextBox.setText("What is your name? ");

        ReplyTextBox = findViewById(R.id.third_msg_txt);//Initialize reply box message


        inputBox = findViewById(R.id.textBox);//initialize input box
        //set the text watcher method on text changed to change the reply text box to set the content of the reply text box to be the inputted name
        inputBox.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                ReplyTextBox.setText("Hello " + s.toString());//change the text when the user starts  typing
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });






    }
}