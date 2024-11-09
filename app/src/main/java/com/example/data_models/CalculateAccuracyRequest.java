package com.example.data_models;

import java.util.List;

public class CalculateAccuracyRequest {
  private String route_name;
  private List<Double> x_coordinates;
  private List<Double> y_coordinates;

  // Constructor
  public CalculateAccuracyRequest(
      String route_name, List<Double> x_coordinates, List<Double> y_coordinates) {
    this.route_name = route_name;
    this.x_coordinates = x_coordinates;
    this.y_coordinates = y_coordinates;
  }

  // Getters and Setters
  public String getRoute_name() {
    return route_name;
  }

  public void setRoute_name(String route_name) {
    this.route_name = route_name;
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
