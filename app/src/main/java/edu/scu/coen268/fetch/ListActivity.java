package edu.scu.coen268.fetch;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    String TAG = this.getClass().getCanonicalName();

    private Intent serviceIntent;
    private ServiceConnection serviceConnection;
    private LookupService lookupService;
    private ListView listView;

    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        listView = findViewById(R.id.list_view);
        listItems.add("Piano");
        listItems.add("Lute");

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        // Do the service bit after the UI is live
        startLookupService();
        bindLookupService();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");

        stopLookupService();
        unbindLookupService();
        super.onDestroy();
    }

    public void addToList(View view) {
        Log.i(TAG, "addToList");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter new item");
        EditText editText = new EditText(this);
        builder.setView(editText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newItem = editText.getText().toString();
                listItems.add(newItem);
                arrayAdapter.notifyDataSetChanged();
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

    public void gotoSettings(View view) {
        Log.i(TAG, "gotoSettings");

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void getStores(View view) {
        Log.i(TAG, "getStores");

        List<Store> stores = lookupService.getStoresForItemList(listItems);
    }

    public void startLookupService() {
        Log.i(TAG, "startLookupService");

        serviceIntent = new Intent(this, LookupService.class);
        startService(serviceIntent);
    }

    public void stopLookupService() {
        Log.i(TAG, "stopLookupService");

        stopService(serviceIntent);
    }

    public void bindLookupService() {
        Log.i(TAG, "bindLookupService");

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LookupService.LookupServiceBinder lookupServiceBinder = (LookupService.LookupServiceBinder) service;
                lookupService = lookupServiceBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // take no action
            }
        };
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void unbindLookupService() {
        Log.i(TAG, "unbindLookupService");

        unbindService(serviceConnection);
        serviceConnection = null;
    }
}
