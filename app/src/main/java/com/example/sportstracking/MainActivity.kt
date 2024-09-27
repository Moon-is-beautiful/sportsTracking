package com.example.sportstracking

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.sportstracking.ui.theme.SportsTrackingTheme

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var isTracking = mutableStateOf(false)
    private var locationData = mutableStateOf("No Data")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationData.value = "Lat: ${location.latitude}, Lon: ${location.longitude}"
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        // Here is where you place the Composable UI inside the setContent block
        setContent {
            SportsTrackingTheme {
                // Add the GPSDisplay Composable UI here
                GPSDisplay(locationData.value, isTracking.value) {
                    if (isTracking.value) {
                        stopLocationUpdates()
                    } else {
                        startLocationUpdates()
                    }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        isTracking.value = true
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    private fun stopLocationUpdates() {
        isTracking.value = false
        locationManager.removeUpdates(locationListener)
    }
}

@Composable
fun GPSDisplay(gpsData: String, isTracking: Boolean, onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "GPS Data: $gpsData", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onButtonClick) {
            Text(if (isTracking) "Stop Tracking" else "Start Tracking")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GPSDisplay("No Data", false, {})
}