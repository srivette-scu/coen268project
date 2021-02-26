package edu.scu.coen268.fetch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch with a splash page
        setTheme(R.style.Theme_fetch);
        setContentView(R.layout.splash_page);

        Intent intent = new Intent(this, FetchList.class);
        startActivity(intent);
    }
}
