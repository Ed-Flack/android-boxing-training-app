package com.example.flackysboxingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView roundLength;
    private TextView restTime;
    private TextView rounds;
    private TextView total;
    private int totalDurationInSeconds;
    private Toast minimumRoundsToast;
    private Toast minimumTimeToast;
    private Toast maximumTimeToast;
    private Toast maximumRoundsToast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.length_minus).setOnClickListener(this);
        findViewById(R.id.length_plus).setOnClickListener(this);
        findViewById(R.id.rest_minus).setOnClickListener(this);
        findViewById(R.id.rest_plus).setOnClickListener(this);
        findViewById(R.id.round_minus).setOnClickListener(this);
        findViewById(R.id.round_plus).setOnClickListener(this);
        findViewById(R.id.play).setOnClickListener(this);
        roundLength = findViewById(R.id.round_length);
        restTime = findViewById(R.id.rest);
        rounds = findViewById(R.id.rounds);
        total = findViewById(R.id.total);
        totalDurationInSeconds = 1140;
        minimumRoundsToast = Toast.makeText(getApplicationContext(), "1 Round Minimum", Toast.LENGTH_SHORT);
        maximumRoundsToast = Toast.makeText(getApplicationContext(), "100 Rounds Maximum", Toast.LENGTH_SHORT);
        minimumTimeToast = Toast.makeText(getApplicationContext(), "5 Seconds Minimum", Toast.LENGTH_SHORT);
        maximumTimeToast = Toast.makeText(getApplicationContext(), "30 Minutes Maximum", Toast.LENGTH_SHORT);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n", "DefaultLocale"})
    @Override
    public void onClick(View v) {
        String roundLength = this.roundLength.getText().toString();
        int roundLengthSeconds = Integer.parseInt(roundLength.split(":")[1]);
        int roundLengthMinutes = Integer.parseInt(roundLength.split(":")[0]);
        String restTime = this.restTime.getText().toString();
        int restTimeSeconds = Integer.parseInt(restTime.split(":")[1]);
        int restTimeMinutes = Integer.parseInt(restTime.split(":")[0]);
        int rounds = Integer.parseInt(this.rounds.getText().toString());
        switch (v.getId()) {
            case R.id.length_minus:
                roundLengthSeconds -= 5;
                if (roundLengthSeconds < 0 && roundLengthMinutes != 0) {
                    roundLengthSeconds = 55;
                    roundLengthMinutes--;
                } else if (roundLengthSeconds < 5 && roundLengthMinutes == 0) {
                    roundLengthSeconds = 5;
                    minimumTimeToast.show();
                }
                this.roundLength.setText(String.format("%d:%02d", roundLengthMinutes, roundLengthSeconds));
                break;
            case R.id.length_plus:
                roundLengthSeconds += 5;
                if (roundLengthSeconds == 60) {
                    roundLengthSeconds = 0;
                    roundLengthMinutes++;
                }
                if (roundLengthMinutes >= 30 && roundLengthSeconds > 0) {
                    roundLengthMinutes = 30;
                    roundLengthSeconds = 0;
                    maximumTimeToast.show();
                }
                this.roundLength.setText(String.format("%d:%02d", roundLengthMinutes, roundLengthSeconds));
                break;
            case R.id.rest_minus:
                restTimeSeconds -= 5;
                if (restTimeSeconds < 0 && restTimeMinutes != 0) {
                    restTimeSeconds = 55;
                    restTimeMinutes--;
                } else if (restTimeSeconds < 5 && restTimeMinutes == 0) {
                    restTimeSeconds = 5;
                    minimumTimeToast.show();
                }
                this.restTime.setText(String.format("%d:%02d", restTimeMinutes, restTimeSeconds));
                break;
            case R.id.rest_plus:
                restTimeSeconds += 5;
                if (restTimeSeconds == 60) {
                    restTimeSeconds = 0;
                    restTimeMinutes++;
                }
                if (restTimeMinutes >= 30 && restTimeSeconds > 0) {
                    restTimeMinutes = 30;
                    restTimeSeconds = 0;
                    maximumTimeToast.show();
                }
                this.restTime.setText(String.format("%d:%02d", restTimeMinutes, restTimeSeconds));
                break;
            case R.id.round_minus:
                rounds--;
                if (rounds < 1) {
                    rounds = 1;
                    minimumRoundsToast.show();
                }
                this.rounds.setText(String.valueOf(rounds));
                break;
            case R.id.round_plus:
                rounds++;
                if (rounds > 100) {
                    rounds = 100;
                    maximumRoundsToast.show();
                }
                this.rounds.setText(String.valueOf(rounds));
                break;
            case R.id.play:
                Intent intent = new Intent(this, StartTime.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roundLengthSeconds", roundLengthSeconds);
                bundle.putInt("roundLengthMinutes", roundLengthMinutes);
                bundle.putInt("restTimeSeconds", restTimeSeconds);
                bundle.putInt("restTimeMinutes", restTimeMinutes);
                bundle.putInt("totalDurationInSeconds", totalDurationInSeconds);
                bundle.putInt("rounds", rounds);
                intent.putExtras(bundle);
                startActivity(intent);
        }
        int roundDurationInSeconds = 60 * roundLengthMinutes + roundLengthSeconds;
        int restTimeDurationInSeconds = 60 * restTimeMinutes + restTimeSeconds;
        totalDurationInSeconds = roundDurationInSeconds * rounds + restTimeDurationInSeconds * (rounds - 1);
        int durationMinutes = totalDurationInSeconds / 60;
        int durationSeconds = totalDurationInSeconds % 60;
        this.total.setText(String.format("Total Time: %d:%02d", durationMinutes, durationSeconds));
    }
}