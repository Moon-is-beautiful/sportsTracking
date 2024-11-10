package com.example.timer_java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IntroPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Button References
        Button buttonOptions = findViewById(R.id.OptionsButton);
        Button buttonProfile = findViewById(R.id.ProfileButton);

        buttonOptions.setOnClickListener(v -> {
            Intent optionsIntent = new Intent(this, OptionsPage.class);
            startActivity(optionsIntent);
        });

        buttonProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        });
    }
}
