package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView newmessage;
    private Button button;
    private EditText type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt); // link to message textview in the Main activity XML
        messageText.setText("Hello World");

        button = findViewById(R.id.test_button);
        type = findViewById(R.id.type_edt);
        newmessage = findViewById(R.id.sub_msg_txt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeText = type.getText().toString();
                messageText.setText("You typed: " + typeText);
            }
        });

        // content fill width of screen (wraps content for width) and has set height 80dp (text is cut off)
        // green sub text
        newmessage.setText("Test_Test_Test_Test_Test_Test_Test_Test_Test_Test_Test_Test_Test_Test_Wrap_contentTest_Test_Test_Test_Test_Test_Test_Test_Test_");
    }
}
