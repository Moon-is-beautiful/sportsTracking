package com.example.timer_java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class OptionsPage extends AppCompatActivity {

    private String selectedOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_options);

        GridLayout optionsContainer = findViewById(R.id.options_container);
        Button confirmButton = findViewById(R.id.confirm_button);

        // Example options with names and image paths
        HashMap<String, Integer> options = new HashMap<>();
        options.put("Hitch", R.drawable.hitch); // Replace with your image resources
        options.put("Option 2", R.drawable.hitch);
        options.put("Option 3", R.drawable.hitch);
        options.put("Option 4", R.drawable.hitch);
        options.put("Option 5", R.drawable.hitch);
        options.put("Option 6", R.drawable.hitch);
        options.put("Option 7", R.drawable.hitch);
        options.put("Option 8", R.drawable.hitch);
        options.put("Option 4", R.drawable.hitch);

        for (String optionName : options.keySet()) {
            // Create a container for each option
            LinearLayout optionLayout = new LinearLayout(this);
            optionLayout.setOrientation(LinearLayout.VERTICAL);
            optionLayout.setPadding(16, 16, 16, 16);
            optionLayout.setBackgroundColor(Color.LTGRAY); // Default background
            optionLayout.setGravity(Gravity.CENTER); // Center content
            optionLayout.setClickable(true);

            // Define layout parameters for grid items
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Spread evenly across columns
            params.height = GridLayout.LayoutParams.WRAP_CONTENT; // Adjust height
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // 1 column, weight = 1
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED); // Automatic row placement
            params.setMargins(16, 16, 16, 16);
            optionLayout.setLayoutParams(params);

            // Add image to the option
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            imageView.setImageResource(options.get(optionName));
            imageView.setPadding(8, 8, 8, 8);

            // Add text to the option
            TextView textView = new TextView(this);
            textView.setText(optionName);
            textView.setTextSize(18);
            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.BLACK);

            // Add views to the option layout
            optionLayout.addView(imageView);
            optionLayout.addView(textView);

            // Add click listener for highlighting
            optionLayout.setOnClickListener(v -> {
                // Reset background for all options
                for (int i = 0; i < optionsContainer.getChildCount(); i++) {
                    LinearLayout child = (LinearLayout) optionsContainer.getChildAt(i);
                    child.setBackgroundColor(Color.LTGRAY);
                }
                // Highlight the selected option
                optionLayout.setBackgroundColor(Color.parseColor("#FFCDD2")); // Light red
                selectedOption = optionName; // Update the selected option
                confirmButton.setVisibility(View.VISIBLE); // Make the button visible
            });

            // Add the option layout to the container
            optionsContainer.addView(optionLayout);
        }

        // Confirm button action
        confirmButton.setOnClickListener(v -> {
            if (selectedOption != null) {
                Intent intent = new Intent(OptionsPage.this, MainActivity.class);
                intent.putExtra("selectedOption", selectedOption);
                startActivity(intent);
            }
        });
    }
}
