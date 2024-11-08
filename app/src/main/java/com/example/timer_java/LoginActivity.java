package com.example.timer_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.data_models.*;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ApiService apiService; // Declare ApiService as an instance variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Use the layout for the login screen

        nameEditText = findViewById(R.id.nameEditTextR);
        usernameEditText = findViewById(R.id.usernameEditTextR);
        passwordEditText = findViewById(R.id.passwordEditTextR);

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
                        verifyLogin(loginInfo);

                        // If login is successful, redirect to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the login activity so the user cannot return to it
                    }
                });
    }

    private void verifyLogin(LoginRequest loginInfo) {
        Call<AuthenticationResponse> call = apiService.login(loginInfo);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (response.isSuccessful()) {
                    // Login successful
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Redirect to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the login activity
                } else {
                    // Handle unsuccessful response (e.g., invalid credentials)
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                // Handle network failure or other errors
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}