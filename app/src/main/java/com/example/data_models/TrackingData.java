package com.example.data_models;

public class TrackingData {
    private long timestamp;
    private double latitude;
    private double longitude;

    // Constructors
    public TrackingData(long timestamp, double latitude, double longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
