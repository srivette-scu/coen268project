package edu.scu.coen268.fetch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        // Launch with a splash page
        setTheme(R.style.Theme_fetch);
        setContentView(R.layout.splash_layout);

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
