package org.codegrinders.treasure_hunter_mobile.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroInstance;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;
import org.codegrinders.treasure_hunter_mobile.model.RegisterRequest;
import org.codegrinders.treasure_hunter_mobile.model.RegisterResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRegister extends AppCompatActivity {
    EditText etUsername, etEmail, etPassword, etConfirmPassword;
    Button bt_submit;
    AwesomeValidation emailValidation;
    MediaService audioService;

    boolean isBound = false;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=\\S+$).{3,99}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,99}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        bt_submit = findViewById(R.id.bt_submit);


        emailValidation = new AwesomeValidation(ValidationStyle.BASIC);
        emailValidation.addValidation(this, R.id.et_email, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        bt_submit.setOnClickListener(v -> {
            audioService.play(Sound.buttonSound, 0);
            if (emailValidation.validate() && validate()) {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setEmail(etEmail.getText().toString());
                registerRequest.setUsername(etUsername.getText().toString());
                registerRequest.setPassword(etPassword.getText().toString());
                registerUser(registerRequest);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Validation Failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser(RegisterRequest registerRequest) {

        Call<RegisterResponse> registerResponseCall = RetroInstance.initializeAPIService().registerUser(registerRequest);

        registerResponseCall.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    String message = "Successfully Registered";
                    Toast.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Gson gson = new Gson();
                        assert response.errorBody() != null;
                        RegisterResponse registerResponse = gson.fromJson(response.errorBody().string(), RegisterResponse.class);
                        String message = registerResponse.getMessage();
                        Toast.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<RegisterResponse> call, @NotNull Throwable t) {

                String message = "Registration failed miserably!";
                Toast.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        String usernameInput = etUsername.getText().toString();
        String passwordInput = etPassword.getText().toString();
        String confirmPasswordInput = etConfirmPassword.getText().toString();

        if (USERNAME_PATTERN.matcher(usernameInput).matches() || usernameInput.contains(" ")) {
            etUsername.setError("Username must be 3–99 characters long without spaces");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches() || passwordInput.contains(" ")) {
            etPassword.setError("Password must be 8–99 characters long and must have at least 1 digit, 1 upper case letter and 1 special character without spaces");
            return false;
        } else if (!confirmPasswordInput.equals(passwordInput)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        } else {
            etPassword.setError(null);
            return true;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            audioService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}
