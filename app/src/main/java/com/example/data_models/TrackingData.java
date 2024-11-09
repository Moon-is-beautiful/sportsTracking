package com.example.data_models;

import java.util.ArrayList;
import java.util.List;

public class TrackingData {
  private List<Double> timestamp;
  private List<Double> x_coordinates;
  private List<Double> y_coordinates;

  // Constructors
  public TrackingData() {
    this.timestamp = new ArrayList<>();
    this.x_coordinates = new ArrayList<>();
    this.y_coordinates = new ArrayList<>();
  }

  // Getters and setters
    //not sure if needed
  public List<Double> getTimestamp() {
    return timestamp;
  }
    //not sure if needed
  public List<Double> getX_coordinates() {
    return x_coordinates;
  }
    //not sure if needed
  public List<Double> getY_coordinates() {
    return y_coordinates;
  }

  public void addData(double time, double x, double y) {
    timestamp.add(time);
    x_coordinates.add(x);
    y_coordinates.add(y);
  }
}
