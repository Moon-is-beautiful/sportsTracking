package com.example.data_models;

public class TrackingAngle {
  private double angle;

  public TrackingAngle() {
    // Default constructor needed for Retrofit/Gson
  }

  public TrackingAngle(double angle) {
    this.angle = angle;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }
}
