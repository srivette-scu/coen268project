package edu.scu.coen268.fetch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch with a splash page
        setTheme(R.style.Theme_fetch);
        setContentView(R.layout.splash_layout);

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
