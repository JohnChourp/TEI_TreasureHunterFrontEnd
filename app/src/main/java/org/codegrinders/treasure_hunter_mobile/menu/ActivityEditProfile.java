package org.codegrinders.treasure_hunter_mobile.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.codegrinders.treasure_hunter_mobile.R;

public class ActivityEditProfile extends AppCompatActivity {

    Button bt_editEmail, bt_editPassword, bt_goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        bt_editEmail = findViewById(R.id.bt_editEmail);
        bt_editPassword = findViewById(R.id.bt_editPassword);
        bt_goBack = findViewById(R.id.bt_goBack);

        bt_editEmail.setOnClickListener(v -> openActivityEditEmail());
        bt_editPassword.setOnClickListener(v -> openActivityChangePassword());
        bt_goBack.setOnClickListener(v -> returnOnActivityUserMenu());
    }

    private void openActivityEditEmail() {
        Intent intent = new Intent(this, ActivityEditEmail.class);
        startActivity(intent);
    }

    private void openActivityChangePassword() {
        Intent intent = new Intent(this, ActivityEditPassword.class);
        startActivity(intent);
    }

    private void returnOnActivityUserMenu() {
        finish();
    }
    }
