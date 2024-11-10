package com.example.timer_java;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.ApiService;
import com.example.data_models.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
  private ApiService apiService;
  private TextView timerTextView, gpsTextView;
  private Button startButton;
  private boolean timerRunning = false;
  private long startTimeInMillis; // Used to store the starting time
  private Handler handler = new Handler(); // Handler for updating the UI every second
  private FusedLocationProviderClient fusedLocationClient;
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

  private TrackingData trackingData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Find views
    timerTextView = findViewById(R.id.timerTextView);
    gpsTextView = findViewById(R.id.GPSTextView);
    startButton = findViewById(R.id.startButton);

    // initialize trackingData to store x, y, and time coordinates
    trackingData = new TrackingData();

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // url for testing on emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    apiService = retrofit.create(ApiService.class);

    // Initialize FusedLocationProviderClient
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    // Initialize locationRequest ~ old functions were deprecated from googleAPI
    locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).setMinUpdateIntervalMillis(500).build();
    // Define LocationCallback
      locationCallback = new LocationCallback() {
          @Override
          public void onLocationResult(LocationResult locationResult) {
              if (locationResult == null) {
                  return;
              }
              for (Location location : locationResult.getLocations()) {
                  if (location != null && timerRunning) {
                      String gpsData = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                      gpsTextView.setText(gpsData);

                      // Calculate elapsed time
                      long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis;

                      // Store data into trackingData data model
                      trackingData.addData(elapsedTimeInMillis, location.getLongitude(), location.getLatitude());
                  }
              }
          }
      };


    // Set up button click listener
    startButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (timerRunning) {
              pauseTracking();
            } else {
              startTracking();
            }
          }
        });
    Button resetButton = findViewById(R.id.resetButton);
    resetButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            resetTimer();
          }
        });
  }

    private void resetTimer() {
        // Stop the timer and location updates if running
        if (timerRunning) {
            handler.removeCallbacks(timerRunnable);
            fusedLocationClient.removeLocationUpdates(locationCallback);
            timerRunning = false;
        }

        startButton.setText("Start");
        gpsTextView.setText("GPS Data");
        startTimeInMillis = 0;

        // Reset timer to 00:00:00
        timerTextView.setText("00:00:00");

        // Clear tracking data
        trackingData.clearData(); // Ensure you have a method to clear data
    }


  // Start the timer and fetch GPS data (combined method)
  private void startTracking() {
    // Check for location permission
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          this,
          new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
          LOCATION_PERMISSION_REQUEST_CODE);
    } else {
        // Start timer and location updates
        startTimer();
        startLocationUpdates();
    }
  }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null // Looper
        );
    }



    // Fetch location and start the timer
    // Start the timer
    private void startTimer() {
        startTimeInMillis = System.currentTimeMillis();
        handler.postDelayed(timerRunnable, 0);
        timerRunning = true;
        startButton.setText("Pause");
        gpsTextView.setText("Tracking..."); //@TODO replace with the GPS data
    }


//  private void fetchLocationAndStartTimer() {
//    // Fetch GPS data
//      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//          // TODO: Consider calling
//          //    ActivityCompat#requestPermissions
//          // here to request the missing permissions, and then overriding
//          //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//          //                                          int[] grantResults)
//          // to handle the case where the user grants the permission. See the documentation
//          // for ActivityCompat#requestPermissions for more details.
//          return;
//      }
//      fusedLocationClient
//        .getLastLocation()
//        .addOnSuccessListener(
//            this,
//                location -> {
//                  if (location != null) {
//                    String gpsData =
//                        "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
//                    gpsTextView.setText(gpsData);
//
//                    // data type is long instead of double in the database
//                    long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis;
//                    long elapsedSeconds = elapsedTimeInMillis / 1000;
//
//                    // Store data into front-end -> i.e.) add to trackingData data model
//                    trackingData.addData(
//                        elapsedTimeInMillis, location.getLongitude(), location.getLatitude());
//                  } else {
//                    gpsTextView.setText("Unable to fetch location");
//                  }
//                });
//
//    // Start the count-up timer
//    startTimeInMillis = System.currentTimeMillis();
//    handler.postDelayed(timerRunnable, 0);
//
//    timerRunning = true;
//    startButton.setText("Pause");
//  }

  // Send GPS data to the backend -> called on 'comparison' button being pressed
  private void sendTrackingDataToBackend() {
    Call<Void> call = apiService.sendTrackingData(this.trackingData);

    call.enqueue(
        new Callback<Void>() {
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
            Toast.makeText(
                    MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT)
                .show();
          }
        });
  }

// Runnable for updating the timer every second
  private Runnable timerRunnable = new Runnable() {
      @Override
      public void run() {
          long elapsedTime = System.currentTimeMillis() - startTimeInMillis;
          updateTimerText(elapsedTime);
          handler.postDelayed(this, 1000); // Update every 1 second
      }
  };


  // Pause the timer
  private void pauseTracking() {
      // Stop the timer
      handler.removeCallbacks(timerRunnable);
      timerRunning = false;
      startButton.setText("Start");

      // Stop location updates
      fusedLocationClient.removeLocationUpdates(locationCallback);

      gpsTextView.setText("Tracking Paused");

      // Optionally, send trackingData to the backend here
      // sendTrackingDataToBackend();
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
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
