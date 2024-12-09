package com.example.timer_java;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ApiService;
import com.example.data_models.FootballRoute;
import com.example.data_models.TrackingAngle;
import com.example.data_models.TrackingData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalibratePage extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private ApiService apiService;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private TextView gpsTextView;
    private Button calibrateButton;
    private boolean trackingActive = false;
    private long startTimeInMillis;
    private Handler handler = new Handler();

    // Data models
    private TrackingData trackingData = new TrackingData();
    private TrackingAngle calibratedAngle;
    private FootballRoute fetchedRoute;
    String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation_page); // Ensure this matches your layout name

        // Retrieve the selected option from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedOption")) {
            selectedOption = intent.getStringExtra("selectedOption");
            Log.d("CalibratePage", "Selected Football Route: " + selectedOption);
        } else {
            selectedOption = null;
            Toast.makeText(this, "No Football Route selected", Toast.LENGTH_SHORT).show();
            // Optionally handle the case when no route is passed
        }

        // Initialize UI references
        gpsTextView = findViewById(R.id.OrientationGPSTextView);
        calibrateButton = findViewById(R.id.CalibrateButton);

        // Initialize Retrofit and ApiService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:80/") // Localhost for emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up location request
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build();

        // Set up location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || !trackingActive) return;
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        String gpsData = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                        gpsTextView.setText(gpsData);

                        long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis;
                        trackingData.addData(elapsedTimeInMillis, location.getLongitude(), location.getLatitude());
                    }
                }
            }
        };

        // Set calibrate button functionality
        calibrateButton.setOnClickListener(v -> {
            if (!trackingActive) {
                startTracking();
            } else {
                stopTracking();
                // After route fetched callback and ensuring we have enough data:
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("userX", trackingData.getX_coordinates());
                requestBody.put("userY", trackingData.getY_coordinates());
                calibrateAngle(requestBody, () -> {
                    Toast.makeText(CalibratePage.this, "Angle Calibrated: "
                                    + (calibratedAngle != null ? calibratedAngle.getAngle() : "N/A"),
                            Toast.LENGTH_SHORT).show();

                    // Fetch route based on selectedOption (assumed you've stored it already)
                    if (selectedOption != null && !selectedOption.isEmpty()) {
                        getRoute(selectedOption, () -> {
                            if (fetchedRoute != null) {
                                Toast.makeText(CalibratePage.this,
                                        "Route fetched: " + fetchedRoute.getRouteName(),
                                        Toast.LENGTH_SHORT).show();

                                // Adjust ideal coordinates if necessary
                                List<double[]> idealCoords = getIdealCoordinatesForSlant();

                                // Here you can perform any adjustments to idealCoords using calibratedAngle
                                // For example, if you need rotation invariance or alignment

                                // Once ready, navigate to MainActivity with the data
                                double angleToPass = (calibratedAngle != null) ? calibratedAngle.getAngle() : 0.0;
                                navigateToMainActivity(angleToPass);
                            } else {
                                Toast.makeText(CalibratePage.this, "Failed to fetch route", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CalibratePage.this, "No route selected to fetch", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    //@TODO implement calibrateAngle function
    public void calibrateAngle(Map<String, Object> requestBody, Runnable callback) {
        Call<TrackingAngle> call = apiService.calibrateAngle(requestBody);
        call.enqueue(new Callback<TrackingAngle>() {
            @Override
            public void onResponse(Call<TrackingAngle> call, Response<TrackingAngle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    calibratedAngle = response.body();
                    Log.d("CalibrateAngle", "Angle calibrated: " + calibratedAngle.getAngle());
                } else {
                    Log.e("CalibrateAngle", "Failed to calibrate angle: " + response.message());
                }
                if (callback != null) callback.run();
            }

            @Override
            public void onFailure(Call<TrackingAngle> call, Throwable t) {
                Log.e("CalibrateAngle", "Network error: " + t.getMessage(), t);
                if (callback != null) callback.run();
            }
        });
    }


    private void navigateToMainActivity(double angle) {
        Intent intent = new Intent(CalibratePage.this, MainActivity.class);

        // Put the angle as a double extra
        intent.putExtra("calibratedAngle", angle);
        intent.putExtra("selectedOption", selectedOption);

        // Convert the List<double[]> to separate arrays or a single flattened array
        // Example: flatten into two arrays: one for x, one for y
//        double[] xCoords = new double[idealCoordinates.size()];
//        double[] yCoords = new double[idealCoordinates.size()];
//        for (int i = 0; i < idealCoordinates.size(); i++) {
//            xCoords[i] = idealCoordinates.get(i)[0];
//            yCoords[i] = idealCoordinates.get(i)[1];
//        }
//
//        intent.putExtra("idealXCoords", xCoords);
//        intent.putExtra("idealYCoords", yCoords);

        startActivity(intent);
//        finish(); // Optional, if you don't want to go back to CalibratePage
    }


    //@TODO implement getRoute function so that I can use the calibrated straight line against that to determine the accuracy
    public void getRoute(String routeName, Runnable callback) {
        Call<FootballRoute> call = apiService.getFootballRoute(routeName);
        call.enqueue(new Callback<FootballRoute>() {
            @Override
            public void onResponse(Call<FootballRoute> call, Response<FootballRoute> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchedRoute = response.body();
                    Log.d("GetRoute", "Fetched route: " + fetchedRoute.getRouteName());
                } else {
                    Log.e("GetRoute", "Failed to fetch route: " + response.message());
                }
                if (callback != null) callback.run();
            }

            @Override
            public void onFailure(Call<FootballRoute> call, Throwable t) {
                Log.e("GetRoute", "Network error: " + t.getMessage(), t);
                if (callback != null) callback.run();
            }
        });
    }

    //@TODO need to put in ideal coordinates for a slant route or whatever route we want to demo
    public List<double[]> getIdealCoordinatesForSlant() {
        // Example: A simple slant route going diagonally across the field
        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{0.0, 0.0});
        coords.add(new double[]{5.0, 1.0});
        coords.add(new double[]{10.0, 2.0});
        coords.add(new double[]{15.0, 3.0});
        return coords;
    }

    private void startTracking() {
        // Check location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            // Start tracking user location
            startTimeInMillis = System.currentTimeMillis();
            trackingActive = true;
            gpsTextView.setText("Tracking...");
            startLocationUpdates();
            calibrateButton.setText("Stop & Calibrate");
        }
    }

    private void stopTracking() {
        // Stop location updates
        fusedLocationClient.removeLocationUpdates(locationCallback);
        trackingActive = false;
        gpsTextView.setText("Tracking Stopped");
        calibrateButton.setText("Calibrate");
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted yet
            return;
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, null
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTracking();
            } else {
                Toast.makeText(this, "Location permission is required for calibration.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
