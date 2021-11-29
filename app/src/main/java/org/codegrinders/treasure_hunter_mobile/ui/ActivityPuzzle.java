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
import org.codegrinders.treasure_hunter_mobile.model.Puzzle;
import org.codegrinders.treasure_hunter_mobile.retrofit.PuzzlesCall;
import org.codegrinders.treasure_hunter_mobile.retrofit.RetroCallBack;
import org.codegrinders.treasure_hunter_mobile.settings.MediaService;
import org.codegrinders.treasure_hunter_mobile.settings.Sound;

public class ActivityPuzzle extends AppCompatActivity {
    Button bt_continue;
    TextView tv_question;
    TextView tv_puzzlePoints;
    EditText et_answer;

    PuzzlesCall puzzlesCall = new PuzzlesCall();

    Puzzle puzzle;
    MediaService audioService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        bt_continue = findViewById(R.id.bt_continue);
        tv_question = findViewById(R.id.tv_question);
        tv_puzzlePoints = findViewById(R.id.tv_puzzlePoints);
        et_answer = findViewById(R.id.et_answer);

        RetroCallBack retroCallBack = new RetroCallBack() {
            @Override
            public void onCallFinished(String callType) {
                if (callType.equals("Puzzles")) {
                    tv_question.setText(puzzlesCall.getQuestion());
                    tv_puzzlePoints.setText(getPuzzlePoints());
                }
                if (callType.equals("postAnswer")) {
                    puzzle = puzzlesCall.getPuzzle();
                    if (et_answer.getText().toString().equals(puzzle.getAnswer())) {
                        Toast.makeText(getApplicationContext(), ActivityMap.currentMarkerData.getDescription(), Toast.LENGTH_LONG).show();
                        ActivityMap.currentMarker.setVisible(false);
                        ActivityMap.currentMarkerData.setVisibility(false);
                        audioService.play(Sound.correctSound, 0);
                        finish();
                    } else {
                        et_answer.setText("");
                        Toast.makeText(getApplicationContext(), "WRONG", Toast.LENGTH_LONG).show();
                        audioService.play(Sound.wrongSound, 0);
                    }

                }
            }

            @Override
            public void onCallFailed(String errorMessage) {
                tv_question.setText(errorMessage);
            }
        };
        puzzlesCall.setCallBack(retroCallBack);
        puzzlesCall.puzzlesGetRequest();
        bt_continue.setOnClickListener(v -> puzzlesCall.puzzleIsCorrect(et_answer.getText().toString()));
    }

    private String getPuzzlePoints() {
        return "Puzzle's points: " + puzzlesCall.getPoints();
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