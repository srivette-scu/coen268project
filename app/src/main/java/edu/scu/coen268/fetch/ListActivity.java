package edu.scu.coen268.fetch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.scu.coen268.fetch.lookupservice.LookupService;
import edu.scu.coen268.fetch.lookupservice.Store;
import edu.scu.coen268.fetch.lookupservice.TestData;

public class ListActivity extends AppCompatActivity {
    String TAG = this.getClass().getCanonicalName();

    private Intent serviceIntent;
    private ServiceConnection serviceConnection;
    private LookupService lookupService;
    private ListView listView;

    private ItemsLists itemsLists = new ItemsLists();
    private static String MAIN_LIST = "main";
    private static String STALE_LIST = "stale";

    private static List<String> defaultList = new ArrayList<>();
    static {
        defaultList.add("Fish");
        defaultList.add("Cheese");
        defaultList.add("Soap");
    }

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView = findViewById(R.id.list_view);

        itemsLists.createList(STALE_LIST, new ArrayList<>());
        itemsLists.createList(MAIN_LIST, new ArrayList<>());
        itemsLists.setCurrentList(MAIN_LIST);

        itemsLists.addToCurrentList("Fish");
        itemsLists.addToCurrentList("Cheese");
        itemsLists.addToCurrentList("Soap");

        itemsLists.getCurrentList();

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                itemsLists.getCurrentList());
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

    public void setCurrentListActive(View view) {
        Log.i(TAG, "setCurrentListActive");

        itemsLists.setCurrentList(MAIN_LIST);
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                itemsLists.getCurrentList());
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        Log.i(TAG, "debug");
    }

    public void setStaleListActive(View view) {
        Log.i(TAG, "setStaleListActive");

        itemsLists.setCurrentList(STALE_LIST);
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                itemsLists.getCurrentList());
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        Log.i(TAG, "debug");
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

                if (itemsLists.getCurrentList().stream()
                        .map(item -> item.toLowerCase())
                        .collect(Collectors.toList())
                        .contains(newItem.toLowerCase())) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Lists don't allow duplicate entries",
                            Toast.LENGTH_SHORT).show();
                } else {
                    arrayAdapter.add(newItem);
                }
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

    public List<String> getCheckedItems() {
        Log.i(TAG, "getCheckedItems");

        List<String> selectedItems = new ArrayList<>();
        SparseBooleanArray selectedPostions = listView.getCheckedItemPositions();

        for (int i=0; i<itemsLists.getCurrentList().size(); i++) {
            if (selectedPostions.get(i)) {
                selectedItems.add(itemsLists.getCurrentList().get(i));
            }
        }

        return selectedItems;
    }

    public void deleteItems(View view) {
        Log.i(TAG, "deleteItems");

        List<String> itemsToDelete = getCheckedItems();
        for (String itemToDelete : itemsToDelete) {
            itemsLists.removeFromCurrentList(itemToDelete);
            arrayAdapter.remove(itemToDelete);
        }
    }

    public void gotoSettings(View view) {
        Log.i(TAG, "gotoSettings");

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void gotoMaps(View view) {
        Log.i(TAG, "gotoMaps");

        List<Store> stores = lookupService.getStoresForItemList(itemsLists.getCurrentList(), getCurrentLocation());

        if (stores.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "No stores within search radius, try expanding your search.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        LatLng currentLocation = getCurrentLocation();

        StringBuilder mapsAddressBuilder = new StringBuilder("https://www.google.com/maps/dir");
        mapsAddressBuilder.append("/" + currentLocation.latitude + "," + currentLocation.longitude);

        for (Store store : stores) {
            mapsAddressBuilder.append("/" + store.getLatLng().latitude + "," + store.getLatLng().longitude);
        }

        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsAddressBuilder.toString()));
        startActivity(mapsIntent);
    }

    public LatLng getCurrentLocation() {
        Log.i(TAG, "getCurrentLocation");

        boolean isDemo = ((FetchApplication) this.getApplication()).isDemo();

        if (!isDemo) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

        return TestData.scuLatLng;
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
