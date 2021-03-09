package edu.scu.coen268.fetch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    String TAG = this.getClass().getCanonicalName();

    public List<String> feedbackAndHelpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }

    public void walkingDistance(View view) {
        Log.i(TAG, "walkingDistance");

        ((FetchApplication) this.getApplication()).setSearchRadiusMiles(1);
    }

    public void bikingDistance(View view) {
        Log.i(TAG, "bikingDistance");

        ((FetchApplication) this.getApplication()).setSearchRadiusMiles(4);
    }

    public void drivingDistance(View view) {
        Log.i(TAG, "drivingDistance");

        ((FetchApplication) this.getApplication()).setSearchRadiusMiles(7);
    }

    public void longDistance(View view) {
        Log.i(TAG, "longDistance");

        ((FetchApplication) this.getApplication()).setSearchRadiusMiles(50);
    }

    public void helpAndFeedback(View view) {
        Log.i(TAG, "helpAndFeedback");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("We welcome your feedback and questions");
        EditText editText = new EditText(this);
        builder.setView(editText);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newItem = editText.getText().toString();
                feedbackAndHelpList.add(newItem);
                Toast.makeText(SettingsActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ignore
            }
        });

        builder.show();
    }

    public void termsAndConditions(View view) {
        Log.i(TAG, "termsAndConditions");

        Intent intent = new Intent(this, TermsAndConditionsActivity.class);
        startActivity(intent);
    }
}
