package com.example.timer_java;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;
import okhttp3.ResponseBody;


public interface ApiService{
	@POST("/createaccount")
	Call<ResponseBody> createAccount(@Body User user);

	
	@POST("/login")
	Call<AuthenticationResponse> login(@Body LoginRequest loginRequest);
	
	@GET("/getFootballRoute")
	Call<FootballRoute> getFootballRoute(@Query("routeName") String routeName);
	
	@POST("/startRoute")
	Call<Void> sendTrackingData(@Body TrackingData data);
}
