package com.example.timer_java;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.widget.Toast;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView timerTextView, gpsTextView;
    private Button startButton;
    private boolean timerRunning = false;
    private long startTimeInMillis;  // Used to store the starting time
    private Handler handler = new Handler();  // Handler for updating the UI every second
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")  //url for testing on emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiService = retrofit.create(ApiService.class);


        // Find views
        timerTextView = findViewById(R.id.timerTextView);
        gpsTextView = findViewById(R.id.GPSTextView);
        startButton = findViewById(R.id.startButton);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up button click listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTracking();
                }
            }
        });
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void resetTimer() {
        handler.removeCallbacks(timerRunnable);  // Stop the handler
        timerRunning = false;
        startButton.setText("Start");
        gpsTextView.setText("GPS Data");
        startTimeInMillis=0;
        // Reset timer to 00:00:00
        timerTextView.setText("00:00:00");
    }

    // Start the timer and fetch GPS data (combined method)
    private void startTracking() {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Start timer and fetch GPS data
            fetchLocationAndStartTimer();
        }
    }

    // Fetch location and start the timer
    private void fetchLocationAndStartTimer() {
        // Fetch GPS data
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String gpsData = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                    gpsTextView.setText(gpsData);

                    // Send data to back-end
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date());
                    TrackingData trackingData = new TrackingData(timestamp, location.getLatitude(), location.getLongitude());
                    sendTrackingDataToBackend(trackingData);
                } 
                else {
                    gpsTextView.setText("Unable to fetch location");
                }
            }
        });

        // Start the count-up timer
        startTimeInMillis = System.currentTimeMillis();
        handler.postDelayed(timerRunnable, 0);

        timerRunning = true;
        startButton.setText("Pause");
    }

    // send gps data to the backend -> currently called when start is pressed on timer
    private void sendTrackingDataToBackend(TrackingData trackingData) {
        Call<Void> call = apiService.sendTrackingData(trackingData);
    
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Data sent successfully
                    Toast.makeText(MainActivity.this, "Data sent to server", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error
                    Toast.makeText(MainActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                }
            }
    
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network error
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Runnable for updating the timer every second
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTimeInMillis;
            updateTimerText(elapsedTime);
            handler.postDelayed(this, 1000);  // Update every 1 second
        }
    };

    // Pause the timer
    private void pauseTimer() {
        handler.removeCallbacks(timerRunnable);  // Stop updating the timer
        timerRunning = false;
        startButton.setText("Start");
        gpsTextView.setText("Stopped Tracking");
    }

    // Update the timer text to show elapsed time
    private void updateTimerText(long elapsedTimeInMillis) {
        int hours = (int) (elapsedTimeInMillis / 1000) / 3600;
        int minutes = (int) ((elapsedTimeInMillis / 1000) % 3600) / 60;
        int seconds = (int) (elapsedTimeInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationAndStartTimer(); // Permission granted, start tracking
            } else {
                gpsTextView.setText("Location permission denied");
            }
        }
    }
}
