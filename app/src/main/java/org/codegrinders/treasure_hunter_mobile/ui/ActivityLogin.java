package org.codegrinders.treasure_hunter_mobile.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;

import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {
    Button bt_login;
    TextView tv_register;
    EditText etUsername, etPassword;

    UsersCall usersCall = new UsersCall();

    MediaService audioService;
    boolean isBound = false;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=\\S+$).{3,99}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,99}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);
        tv_register = findViewById(R.id.tv_register);

        tv_register.setOnClickListener(v -> openActivityRegister());
        bt_login.setOnClickListener(v -> {
            audioService.play(Sound.buttonSound, 0);
            login();
        });
    }

    private void openActivityRegister() {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
    }


    private boolean validate() {
        String usernameInput = etUsername.getText().toString();
        String passwordInput = etPassword.getText().toString();

        if (USERNAME_PATTERN.matcher(usernameInput).matches() || usernameInput.contains(" ")) {
            etUsername.setError("Wrong Username or Email");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches() || passwordInput.contains(" ")) {
            etPassword.setError("Wrong Password");
            return false;
        }  else {
            etPassword.setError(null);
            return true;
        }
    }

    private void openActivityMap() {
        Intent intent = new Intent(this, ActivityMap.class);
        intent.putExtra("User", usersCall.getUser());
        startActivity(intent);
    }

    public void login() {
        boolean res = validate();
        if (res) {
            RetroCallBack retroCallBack = new RetroCallBack() {
                @Override
                public void onCallFinished(String callType) {
                    if (usersCall.getUser() != null) {
                        usersCall.userLoginResponse(usersCall.getUser());
                        openActivityMap();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCallFailed(String errorMessage) {
                }
            };
            usersCall.userLoginRequest(etUsername.getText().toString(), etPassword.getText().toString());
            usersCall.setCallBack(retroCallBack);
        } else {
            Toast.makeText(getApplicationContext(), "Login Failed...", Toast.LENGTH_SHORT).show();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            audioService = binder.getService();
            audioService.stop(Sound.gameMusic);
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