package com.example.timer_java;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Reference to the LinearLayout inside the ScrollView
        LinearLayout historyContainer = findViewById(R.id.historyContainer);

        // Example dynamic data for history
        List<String> historyData = new ArrayList<>();
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");
        historyData.add("Play 1 - 50% accuracy");
        historyData.add("Play 2 - 53% accuracy");
        historyData.add("Play 3 - 60% accuracy");
        historyData.add("Play 4 - 45% accuracy");
        historyData.add("Play 5 - 70% accuracy");


        TextView usernameTextView = findViewById(R.id.titleText);
        usernameTextView.setText("zihao");

        // Dynamically add TextViews to the history container
        for (String play : historyData) {
            TextView playTextView = new TextView(this);
            playTextView.setText(play);
            playTextView.setTextSize(16);
            playTextView.setPadding(0, 8, 0, 8); // Add vertical padding between items

            // Add the TextView to the container
            historyContainer.addView(playTextView);
        }
    }
}
