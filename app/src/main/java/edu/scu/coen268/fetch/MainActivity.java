package edu.scu.coen268.fetch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean showMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch with a splash page
        setTheme(R.style.Theme_fetch);
        setContentView(R.layout.splash_page);

        Timer timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 3000);

        while(!showMain) {
            Log.i("TAG", "not set");
        }
        Log.i("TAG", "set");

        setContentView(R.layout.activity_main);
    }

    public void showMain() {
        showMain = true;
    }

    public void navigateToLinear(View view) {
        Intent intent = new Intent(this, LinearExample.class);
        startActivity(intent);
    }

    public void navigateToRelative(View view) {
        Intent intent = new Intent(this, RelativeExample.class);
        startActivity(intent);
    }

    public void navigateToTable(View view) {
        Intent intent = new Intent(this, TableExample.class);
        startActivity(intent);
    }

    public void navigateToGrid(View view) {
        Intent intent = new Intent(this, GridExample.class);
        startActivity(intent);
    }

    public void navigateToFrame(View view) {
        Intent intent = new Intent(this, FrameExample.class);
        startActivity(intent);
    }

    public void navigateToConstraint(View view) {
        Intent intent = new Intent(this, ConstraintExample.class);
        startActivity(intent);
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            showMain();
        }
    }
}
