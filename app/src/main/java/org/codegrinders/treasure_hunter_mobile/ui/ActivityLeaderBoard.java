package org.codegrinders.treasure_hunter_mobile.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.codegrinders.treasure_hunter_mobile.R;
import org.codegrinders.treasure_hunter_mobile.model.User;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.retrofit.UsersCall;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityLeaderBoard extends AppCompatActivity {

    ListView listOfflinePlayers,listOnlinePlayers;
    Button bt_goBack;
    UsersCall usersCall = new UsersCall();
    MediaService audioService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listOfflinePlayers = findViewById(R.id.listOfflinePlayers);
        //listOnlinePlayers = findViewById(R.id.listOnlinePlayers);
        bt_goBack = findViewById(R.id.bt_goBack2);

        bt_goBack.setOnClickListener((View.OnClickListener) v -> {
            audioService.play(Sound.buttonSound, 0);
            returnToPreviousActivity();
        });

        RetroCallBack retroCallBack = new RetroCallBack() {
            @Override
            public void onCallFinished(String callType) {
                if(callType.equals("Users")){
                    listOfflinePlayers();
                }
//                if(callType.equals("UsersOnline")){
//                    listOnlinePlayers();
//                }
            }

            @Override
            public void onCallFailed(String errorMessage) {
                Toast.makeText(ActivityLeaderBoard.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        };
        usersCall.setCallBack(retroCallBack);
        usersCall.usersGetRequest();
        //usersCall.onlineUsersResponse();
    }

    void listOfflinePlayers() {
        List<User> listOfflinePlayers = usersCall.getUsers();
        String[] leaderBoardOffline = new String[listOfflinePlayers.size()];
        for (int i = 0; i < listOfflinePlayers.size(); i++) {
            leaderBoardOffline[i] = listOfflinePlayers.get(i).getUsername()
                    + " : " + listOfflinePlayers.get(i).getPoints();
        }
        this.listOfflinePlayers.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, leaderBoardOffline));
    }

//    void listOnlinePlayers() {
//        List<User> listOnlinePlayers = usersCall.getUsersOnline();
//        String[] leaderBoardOnline = new String[listOnlinePlayers.size()];
//        for (int i = 0; i < listOnlinePlayers.size(); i++) {
//            leaderBoardOnline[i] = listOnlinePlayers.get(i).getUsername();
//        }
//        this.listOnlinePlayers.setAdapter(new ArrayAdapter<>(getApplicationContext(),
//                android.R.layout.simple_list_item_1, leaderBoardOnline));
//    }

    void returnToPreviousActivity() {
        finish();
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            audioService = binder.getService();
            audioService.stop(Sound.gameMusic);
            audioService.play(Sound.menuMusic, Sound.get(Sound.menuMusic).position);
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
