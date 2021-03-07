package edu.scu.coen268.fetch.lookupservice;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scu.coen268.fetch.FetchApplication;

public class LookupService extends Service {
    String TAG = this.getClass().getCanonicalName();

    private Binder binder = new LookupServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");

        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        TestData.populateDBWithTestData(getDb(true));

        return START_NOT_STICKY;
    }

    public Set<Store> getStoresForItemListDummy(List<String> items, LatLng currentLocation) {
        Set<Store> stores = new HashSet<>();
        stores.add(TestData.target);
        stores.add(TestData.traderJoes);
        stores.add(TestData.wholeFoods);

        return stores;
    }

    public Set<Store> getStoresForItemList(List<String> items, LatLng currentLocation) {
        Log.i(TAG, "getStoresForItemList");

        double searchRadiusMiles = ((FetchApplication) this.getApplication()).getSearchRadiusMiles();

        Set<Store> stores = new HashSet<>();
        for (String item : items) {
            List<Store> foundStores = StoreInfoDBHelper.getStoresForKeyword(getDb(false), item);
            if (!foundStores.isEmpty()) {
                stores.add(pickClosestStoreInRange(foundStores, currentLocation, searchRadiusMiles));
            }
        }

        return stores;
    }

    public Store pickClosestStoreInRange(
            List<Store> stores,
            LatLng currentLatLng,
            double rangeInMiles) {
        Log.i(TAG, "pickClosestStoreInRange");

        // TODO crappy radius search
        return stores.get(0);
    }

    public SQLiteDatabase getDb(boolean writeable) {
        StoreInfoDBHelper dbHelper = new StoreInfoDBHelper(getApplicationContext());
        if (writeable) {
            return dbHelper.getWritableDatabase();
        } else {
            return dbHelper.getReadableDatabase();
        }
    }

    // Utilities //
    public class LookupServiceBinder extends Binder {
        public LookupService getService() {
            return LookupService.this;
        }
    }
}
