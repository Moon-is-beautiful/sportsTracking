package com.example.timer_java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class AccPage extends Activity {
    Button btn2, btn3, btn4;
    TextView tv_Output;
    TextView accuracyOutput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_page_layout);
        double accuracy = 20;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("accuracy")) {
            accuracy = intent.getDoubleExtra("accuracy", 0.0); // Retrieve as double
            accuracyOutput = findViewById(R.id.percentAccuracy);
            accuracyOutput.setText(String.format("%.2f", accuracy));
        } else {
            accuracyOutput = findViewById(R.id.percentAccuracy);
            accuracyOutput.setText("No accuracy score received.");
        }


        btn2 = (Button)findViewById(R.id.backButton);
        btn3 = (Button)findViewById(R.id.saveButton);
        btn4 = (Button)findViewById(R.id.reportButton);

        tv_Output = (TextView)findViewById(R.id.outputLabel);
        accuracyOutput=(TextView)findViewById(R.id.percentAccuracy);
        accuracyOutput.setText(accuracyOutput.getText());

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Output.setText("Save to Database");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Output.setText("Save to Phone");
            }
        });
    }


}
