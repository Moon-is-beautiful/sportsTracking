package com.example;

import com.example.data_models.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface ApiService{
	@POST("/createAccount")
	Call<AuthenticationResponse> createAccount(@Body LoginRequest createAccountRequest);

  @POST("/login")
  Call<AuthenticationResponse> login(@Body LoginRequest loginRequest);

  @GET("/getFootballRoute")
  Call<FootballRoute> getFootballRoute(@Query("routeName") String routeName);

  @POST("/startRoute")
  Call<Void> sendTrackingData(@Body TrackingData data);

  @POST("/calculateAccuracy")
  Call<CalculateAccuracyResponse> calculateAccuracy(@Body CalculateAccuracyRequest request);
}
