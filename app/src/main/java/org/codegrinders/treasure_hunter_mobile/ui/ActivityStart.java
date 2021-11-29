package org.codegrinders.treasure_hunter_mobile.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Settings;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;

public class ActivityStart extends AppCompatActivity {
    Button bt_start;
    Button bt_settings;

    MediaService audioService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bt_settings = findViewById(R.id.bt_settings);
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(v -> {
            audioService.play(Sound.buttonSound, 0);
            openActivityLogin();
        });

        bt_settings.setOnClickListener(v -> {
            audioService.play(Sound.buttonSound, 0);
            openActivitySettings();
        });

    }

    private void openActivityLogin() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }

    private void openActivitySettings() {
        Intent intent = new Intent(this, ActivitySettings.class);
        startActivity(intent);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            audioService = binder.getService();
            isBound = true;

            if (Sound.firstInit) {
                Settings.init(getApplicationContext());
                Sound.menuMusic = Sound.add(R.raw.wanabe_epic_music, "music");
                Sound.gameMusic = Sound.add(R.raw.iek_advert, "music");
                Sound.buttonSound = Sound.add(R.raw.pop, "sound");
                Sound.correctSound = Sound.add(R.raw.correct, "sound");
                Sound.wrongSound = Sound.add(R.raw.hnieh, "sound");
                audioService.init(Sound.menuMusic, Settings.musicVol, true);
                audioService.init(Sound.gameMusic, Settings.musicVol, true);
                audioService.init(Sound.buttonSound, Settings.soundVol, false);
                audioService.init(Sound.correctSound, Settings.soundVol, false);
                audioService.init(Sound.wrongSound, Settings.soundVol, false);
                Sound.firstInit = false;
            }
            audioService.play(Sound.menuMusic, Sound.get(Sound.menuMusic).position);
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