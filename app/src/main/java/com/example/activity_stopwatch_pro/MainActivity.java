package com.example.activity_stopwatch_pro;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private ArrayList<Integer> lapTimes = new ArrayList<>();
    private LinearLayout lapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        lapContainer = findViewById(R.id.lap_container);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            lapTimes = (ArrayList<Integer>) savedInstanceState.getSerializable("lapTimes");
            displayLaps();
        }
        runTimer();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        lapTimes.clear();
        lapContainer.removeAllViews();
    }

    public void onClickLap(View view) {
        if (running) {
            lapTimes.add(seconds);
            displayLap(seconds);
        }
    }

    private void displayLap(int lapTime) {
        TextView lapView = new TextView(this);
        int hours = lapTime / 3600;
        int minutes = (lapTime % 3600) / 60;
        int secs = lapTime % 60;
        String time = String.format(Locale.getDefault(), "Vuelta %d: %d:%02d:%02d", lapTimes.size(), hours, minutes, secs);
        lapView.setText(time);
        lapContainer.addView(lapView);
    }

    private void displayLaps() {
        for (int lapTime : lapTimes) {
            displayLap(lapTime);
        }
    }

    private void runTimer() {
        final TextView timeView = findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putSerializable("lapTimes", lapTimes);
    }
}