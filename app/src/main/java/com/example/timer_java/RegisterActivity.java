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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText create_nameEditText;
    private EditText create_usernameEditText;
    private EditText create_passwordEditText;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:80/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        apiService = retrofit.create(ApiService.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        create_nameEditText = findViewById(R.id.nameEditTextR);
        create_usernameEditText = findViewById(R.id.usernameEditTextR);
        create_passwordEditText = findViewById(R.id.passwordEditTextR);

        Button registerButton = findViewById(R.id.RegisterButton);

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = create_nameEditText.getText().toString().trim();
                        String username = create_usernameEditText.getText().toString().trim();
                        String password = create_passwordEditText.getText().toString().trim();

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
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Unable to create account", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
