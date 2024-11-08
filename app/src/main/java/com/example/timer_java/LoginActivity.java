package com.example.timer_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ApiService;
import com.example.data_models.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

  private EditText login_nameEditText;
  private EditText login_usernameEditText;
  private EditText login_passwordEditText;
  private EditText create_nameEditText;
  private EditText create_usernameEditText;
  private EditText create_passwordEditText;

  private ApiService apiService; // Declare ApiService as an instance variable

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login); // Use the layout for the login screen

    create_nameEditText = findViewById(R.id.nameEditTextR);
    create_usernameEditText = findViewById(R.id.usernameEditTextR);
    create_passwordEditText = findViewById(R.id.passwordEditTextR);

    login_nameEditText = findViewById(R.id.nameEditText);
    login_usernameEditText = findViewById(R.id.usernameEditText);
    login_passwordEditText = findViewById(R.id.passwordEditText);

    Button loginButton = findViewById(R.id.loginButton);
    Button registerButton = findViewById(R.id.RegisterButton);

    loginButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // TODO: Add login conditions here (like checking username/password)
            String name = login_nameEditText.getText().toString().trim();
            String username = login_usernameEditText.getText().toString().trim();
            String password = login_passwordEditText.getText().toString().trim();

            // check if any of the inputs are empty
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

            // If login is successful, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the login activity so the user cannot return to it
          }
        });

    registerButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // TODO: Add login conditions here (like checking username/password)
            String name = create_nameEditText.getText().toString().trim();
            String username = create_usernameEditText.getText().toString().trim();
            String password = create_passwordEditText.getText().toString().trim();

            // check if any of the inputs are empty
            if (name.isEmpty()) {
              create_nameEditText.setError("Name is required");
              create_nameEditText.requestFocus();
              return;
            }
            if (username.isEmpty()) {
              create_usernameEditText.setError("Username is required");
              create_usernameEditText.requestFocus();
              return;
            }
            if (password.isEmpty()) {
              create_passwordEditText.setError("Password is required");
              create_passwordEditText.requestFocus();
              return;
            }

            LoginRequest newUserInfo = new LoginRequest(name, username, password);
            createAccount(newUserInfo);

            // If login is successful, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the login activity so the user cannot return to it
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
            if (response.isSuccessful()) {
              // Login successful
              Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

              // Redirect to MainActivity
              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
              startActivity(intent);
              finish(); // Close the login activity
            } else {
              // Handle unsuccessful response (e.g., invalid credentials)
              Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
            // Handle network failure or other errors
            Toast.makeText(
                    LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT)
                .show();
          }
        });
  }

  private void createAccount(LoginRequest newUserInfo) {
    Call<AuthenticationResponse> call = apiService.createAccount(newUserInfo);
    call.enqueue(
        new Callback<AuthenticationResponse>() {
          @Override
          public void onResponse(
              Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
            if (response.isSuccessful()) {
              // Login successful
              Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

              // Redirect to MainActivity
              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
              startActivity(intent);
              finish(); // Close the login activity
            } else {
              // Handle unsuccessful response (e.g., invalid credentials)
              Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
            // Handle network failure or other errors
            Toast.makeText(
                    LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT)
                .show();
          }
        });
  }
}
