package edu.scu.coen268.fetch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.scu.coen268.fetch.lookupservice.TestData;

public class ListActivityHelpers extends AppCompatActivity {
    static String TAG = "ListActivityImpl";

    public static List<String> getCheckedItems(ListView listView, ItemsLists itemsLists) {
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

    public static LatLng getCurrentLocation(
            boolean isDemo,
            LocationManager locationManager,
            Context context,
            Activity activity) {
        Log.i(TAG, "getCurrentLocation");


        if (!isDemo) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

        return TestData.scuLatLng;
    }

    public static ArrayAdapter replaceAdapter(ItemsLists itemsLists, ListView listView, Context context) {
        ArrayAdapter newArrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_multiple_choice,
                itemsLists.getCurrentList());
        listView.setAdapter(newArrayAdapter);
        newArrayAdapter.notifyDataSetChanged();

        return newArrayAdapter;
    }
}
