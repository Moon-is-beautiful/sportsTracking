package com.example.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalculateAccuracyResponse {
  @SerializedName("Accuracy Score")
  private double accuracyScore;
  public CalculateAccuracyResponse(
         double accuracy){
    this.accuracyScore = accuracy;
  }
  // Getter and Setter
  public double getAccuracyScore() {
    return accuracyScore;
  }

  public void setAccuracyScore(double accuracyScore) {
    this.accuracyScore = accuracyScore;
  }
}
