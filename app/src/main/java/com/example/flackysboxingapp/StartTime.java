package com.example.flackysboxingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StartTime extends AppCompatActivity implements View.OnClickListener {

    private TextView time;
    private Toast minimum;
    private Toast maximum;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_time);
        this.time = findViewById(R.id.time);
        findViewById(R.id.prep_plus).setOnClickListener(this);
        findViewById(R.id.prep_minus).setOnClickListener(this);
        findViewById(R.id.prep_play).setOnClickListener(this);
        minimum = Toast.makeText(getApplicationContext(), "5 Seconds Minimum", Toast.LENGTH_SHORT);
        maximum = Toast.makeText(getApplicationContext(), "30 Minutes Maximum", Toast.LENGTH_SHORT);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        int seconds = Integer.parseInt(time.getText().toString().split(":")[1]);
        int minutes = Integer.parseInt(time.getText().toString().split(":")[0]);
        if (v.getId() == R.id.prep_minus) {
            seconds -= 5;
            if (seconds < 0 && minutes != 0) {
                seconds = 55;
                minutes--;
            } else if (seconds < 5 && minutes == 0) {
                seconds = 5;
                minimum.show();
            }
        } else if (v.getId() == R.id.prep_plus) {
            seconds += 5;
            if (seconds == 60) {
                seconds = 0;
                minutes++;
            }
            if (minutes >= 30 && seconds > 0) {
                minutes = 30;
                seconds = 0;
                maximum.show();
            }
        } else {
            Intent intent = new Intent(this, Box.class);
            Bundle bundle = getIntent().getExtras();
            bundle.putInt("prepTimeMinutes", minutes);
            bundle.putInt("prepTimeSeconds", seconds);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        this.time.setText(String.format("%d:%02d", minutes, seconds));
    }
}
