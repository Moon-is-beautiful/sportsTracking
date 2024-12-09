package com.example.timer_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ApiService;
import com.example.data_models.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

  private EditText login_nameEditText;
  private EditText login_usernameEditText;
  private EditText login_passwordEditText;
  private ApiService apiService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:80/") // URL for testing on emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    apiService = retrofit.create(ApiService.class);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    login_nameEditText = findViewById(R.id.nameEditText);
    login_usernameEditText = findViewById(R.id.usernameEditText);
    login_passwordEditText = findViewById(R.id.passwordEditText);

    Button loginButton = findViewById(R.id.loginButton);
    TextView registerLink = findViewById(R.id.registerLink);

    loginButton.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                String name = login_nameEditText.getText().toString().trim();
                String username = login_usernameEditText.getText().toString().trim();
                String password = login_passwordEditText.getText().toString().trim();

                if (name.isEmpty()) {
                  login_nameEditText.setError("Name is required");
                  login_nameEditText.requestFocus();
                  return;
                }
                if (username.isEmpty()) {
                  login_usernameEditText.setError("Username is required");
                  login_usernameEditText.requestFocus();
                  return;
                }
                if (password.isEmpty()) {
                  login_passwordEditText.setError("Password is required");
                  login_passwordEditText.requestFocus();
                  return;
                }

                LoginRequest loginInfo = new LoginRequest(name, username, password);
                verifyLogin(loginInfo);
              }
            });

    registerLink.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
              }
            });
  }

  private void verifyLogin(LoginRequest loginInfo) {
    Call<AuthenticationResponse> call = apiService.login(loginInfo);
    call.enqueue(
            new Callback<AuthenticationResponse>() {
              @Override
              public void onResponse(
                      Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (response.body() != null && response.body().getAuthentication()) {
                  Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                  Intent intent = new Intent(LoginActivity.this, OptionsPage.class);
                  startActivity(intent);
                  finish();
                } else {
                  Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
              }

              @Override
              public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
              }
            });
  }
}
