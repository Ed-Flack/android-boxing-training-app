package com.example.flackysboxingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Box extends AppCompatActivity {

    private TextView activity;
    private TextView time;
    private MediaPlayer tripleBell;
    private MediaPlayer singleBell;
    private MediaPlayer beep;
    private CountDownTimer timer1;
    private CountDownTimer timer2;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        this.activity = findViewById(R.id.activity);
        this.time = findViewById(R.id.box_time);
        this.singleBell = MediaPlayer.create(this, R.raw.single_bell);
        this.tripleBell = MediaPlayer.create(this, R.raw.bell);
        this.beep = MediaPlayer.create(this, R.raw.beep);
        Bundle bundle = getIntent().getExtras();
        //noinspection MalformedFormatString
        this.time.setText(String.format("%d:%02d", bundle.get("prepTimeMinutes"), bundle.get("prepTimeSeconds")));
        int prepTimeInSeconds = (int) bundle.get("prepTimeMinutes") * 60 + (int) bundle.get("prepTimeSeconds");
        int roundLengthMinutes = (int) bundle.get("roundLengthMinutes");
        int roundLengthSeconds = (int) bundle.get("roundLengthSeconds");
        int restTimeMinutes = (int) bundle.get("restTimeMinutes");
        int restTimeSeconds = (int) bundle.get("restTimeSeconds");
        int numberOfRounds = (int) bundle.get("rounds");
        int totalDurationInSeconds = (int ) bundle.get("totalDurationInSeconds");
        this.timer2 = new CountDownTimer(TimeUnit.SECONDS.toMillis(totalDurationInSeconds), TimeUnit.SECONDS.toMillis(1)) {

            private int thisRoundMins = roundLengthMinutes;
            private int thisRoundSeconds = roundLengthSeconds;
            private int currentRound = 1;
            private boolean box = true;

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("Seconds till finished: " + millisUntilFinished / 1000);
                if (thisRoundSeconds == 0) {
                    if (thisRoundMins != 0) {
                        thisRoundSeconds = 59;
                        thisRoundMins--;
                    }
                } else {
                    thisRoundSeconds--;
                }
                time.setText(String.format("%d:%02d", thisRoundMins, thisRoundSeconds));
                if (thisRoundMins == 0 && thisRoundSeconds <= 5 && thisRoundSeconds > 0){
                    beep.start();
                } else if (thisRoundMins == 0 && thisRoundSeconds == 0) {
                    if (box) {
                        tripleBell.start();
                        if (currentRound < numberOfRounds) {
                            activity.setText("Rest");
                            time.setText(String.format("%d:%02d", restTimeMinutes, restTimeSeconds));
                            thisRoundMins = restTimeMinutes;
                            thisRoundSeconds = restTimeSeconds;
                            box = false;
                            currentRound++;
                        }
                    } else {
                        singleBell.start();
                        activity.setText(String.format("Round %d", currentRound));
                        time.setText(String.format("%d:%02d", roundLengthMinutes, roundLengthSeconds));
                        thisRoundMins = roundLengthMinutes;
                        thisRoundSeconds = roundLengthSeconds;
                        box = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                tripleBell.start();
                activity.setText("Finished");
                time.setText("Good Job!");
            }
        };
        this.timer1 = new CountDownTimer(TimeUnit.SECONDS.toMillis(prepTimeInSeconds), TimeUnit.SECONDS.toMillis(1)) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int timeMinutes = seconds / 60;
                int timeSeconds = seconds % 60;
                time.setText(String.format("%d:%02d", timeMinutes, timeSeconds));
                if (seconds <= 5 && seconds > 0) {
                    beep.start();
                } else if (seconds == 0) {
                    singleBell.start();
                    activity.setText("Round 1");
                    time.setText(String.format("%d:%02d", roundLengthMinutes, roundLengthSeconds));
                }
            }

            @Override
            public void onFinish() {
                timer2.start();
            }
        };
        timer1.start();
    }


    @Override
    public void onBackPressed() {
        timer1.cancel();
        timer2.cancel();
        this.finish();
    }
}