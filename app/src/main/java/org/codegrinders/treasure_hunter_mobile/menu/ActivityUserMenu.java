package org.codegrinders.treasure_hunter_mobile.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.ui.ActivityLeaderBoard;

public class ActivityUserMenu extends AppCompatActivity {

    Button bt_resume, bt_editProfile, bt_leaderBoard, bt_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermenu);

        bt_resume = findViewById(R.id.bt_resume);
        bt_editProfile = findViewById(R.id.bt_editProfile);
        bt_leaderBoard = findViewById(R.id.bt_leaderBoardMenu);
        bt_delete = findViewById(R.id.bt_deleteProfile);

        bt_resume.setOnClickListener(v -> returnToActivityMap());
        bt_editProfile.setOnClickListener(v -> openActivityEditProfile());
        bt_leaderBoard.setOnClickListener(v -> openActivityLeaderBoard());
        bt_delete.setOnClickListener(v -> openActivityDeleteAccount());
    }

    private void returnToActivityMap() {
        finish();
    }

    private void openActivityEditProfile() {
        Intent intent = new Intent(this, ActivityEditProfile.class);
        startActivity(intent);
    }

    private void openActivityLeaderBoard() {
        Intent intent = new Intent(this, ActivityLeaderBoard.class);
        startActivity(intent);
    }

    private void openActivityDeleteAccount() {
        Intent intent = new Intent(this, ActivityDeleteAccount.class);
        startActivity(intent);
    }
}