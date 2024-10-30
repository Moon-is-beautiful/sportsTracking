package com.example.timer_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.data_models.*;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login); // Use the layout for the login screen

    nameEditText = findViewByID(R.id.nameEditTextR);
    usernameEditText = findViewByID(R.id.usernameEditTextR);
    passwordEditText = findViewByID(R.id.passwordEditTextR);

    Button loginButton = findViewById(R.id.loginButton);

    loginButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // TODO: Add login conditions here (like checking username/password)
            String name = nameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // check if any of the inputs are empty
            if (name.isEmpty()) {
              nameEditText.setError("Name is required");
              nameEditText.requestFocus();
              return;
            }
            if (username.isEmpty()) {
              usernameEditText.setError("Username is required");
              usernameEditText.requestFocus();
              return;
            }
            if (password.isEmpty()) {
              passwordEditText.setError("Password is required");
              passwordEditText.requestFocus();
              return;
            }

            LoginRequest loginInfo = new LoginRequest(name, username, password);
            boolean result = verifyLogin(loginInfo);

            // If login is successful, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the login activity so the user cannot return to it
          }
        });
  }

  private boolean verifyLogin(LoginRequest loginInfo) {
    Call<Void> call = apiService.login(loginInfo);
    call.enqueue(
        new Callback<Void>() {
          @Override
          public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
              // Data sent successfully
              Toast.makeText(MainActivity.this, "Data sent to server", Toast.LENGTH_SHORT).show();
            } else {
              // Handle error
              Toast.makeText(MainActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFailure(Call<Void> call, Throwable t) {
            // Handle network error
            Toast.makeText(
                    MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT)
                .show();
          }
        });
  }
}
