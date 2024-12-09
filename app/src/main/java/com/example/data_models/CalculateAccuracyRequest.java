package com.example.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalculateAccuracyRequest {
  @SerializedName("routeName")
  private String routeName;
  @SerializedName("x_coordinates")
  private List<Double> x_coordinates;
  @SerializedName("y_coordinates")
  private List<Double> y_coordinates;
  @SerializedName("angle")
  private Double angle;

  // Constructor
  public CalculateAccuracyRequest(
      String route_name, List<Double> x_coordinates, List<Double> y_coordinates, double calibratedAngle) {
    this.routeName = route_name;
    this.x_coordinates = x_coordinates;
    this.y_coordinates = y_coordinates;
    this.angle = calibratedAngle;
  }

  // Getters and Setters
  public String getRoute_name() {
    return routeName;
  }

  public void setRoute_name(String route_name) {
    this.routeName = route_name;
  }

  public List<Double> getX_coordinates() {
    return x_coordinates;
  }

  public void setX_coordinates(List<Double> x_coordinates) {
    this.x_coordinates = x_coordinates;
  }

  public List<Double> getY_coordinates() {
    return y_coordinates;
  }

  public void setY_coordinates(List<Double> y_coordinates) {
    this.y_coordinates = y_coordinates;
  }
}
