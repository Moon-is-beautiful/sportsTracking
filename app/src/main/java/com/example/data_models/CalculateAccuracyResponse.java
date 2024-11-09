package com.example.data_models;

import com.google.gson.annotations.SerializedName;

public class CalculateAccuracyResponse {
  @SerializedName("Accuracy Score")
  private double accuracyScore;

  // Getter and Setter
  public double getAccuracyScore() {
    return accuracyScore;
  }

  public void setAccuracyScore(double accuracyScore) {
    this.accuracyScore = accuracyScore;
  }
}
