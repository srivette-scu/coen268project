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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public List<Store> getStoresForItemList(List<String> items, LatLng currentLocation) {
        Log.i(TAG, "getStoresForItemList");

        double searchRadiusMiles = ((FetchApplication) this.getApplication()).getSearchRadiusMiles();

        Set<Store> stores = new HashSet<>();
        for (String item : items) {
            List<Store> foundStores = StoreInfoDBHelper.getStoresForKeyword(getDb(false), item);
            if (!foundStores.isEmpty()) {
                Optional<Store> maybeStore = pickClosestStoreInRange(foundStores, currentLocation, searchRadiusMiles);
                if (maybeStore.isPresent()) {
                    stores.add(maybeStore.get());
                }
            }
        }

        // Sort the stores by distance to prevent awful routing problems
        return sortStoresClosest(new ArrayList<>(stores), currentLocation);
    }

    public Optional<Store> pickClosestStoreInRange(
            List<Store> stores,
            LatLng currentLatLng,
            double rangeInMiles) {
        Log.i(TAG, "pickClosestStoreInRange");

        // Order the stores by proximity
        stores = sortStoresClosest(stores, currentLatLng);
        
        if (getDistanceMiles(stores.get(0).getLatLng(), currentLatLng) <= rangeInMiles) {
            return Optional.of(stores.get(0));
        }

        return Optional.empty();
    }

    public List<Store> sortStoresClosest(List<Store> stores, LatLng origin) {
        Log.i(TAG, "sortStoresClosest");
        
        // Order the stores by proximity
        stores.sort(new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                double dist1 = getDistanceMiles(o1.getLatLng(), origin);
                double dist2 = getDistanceMiles(o2.getLatLng(), origin);
                if (dist1 < dist2) {
                    return -1;
                } else if (dist1 > dist2) {
                    return 1;
                }
                return 0;
            }
        });

        return stores;
    }

    // This method was adapted from
    // https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
    // because re-deriving it is silly
    public double getDistanceMiles(LatLng origin, LatLng dest) {
        Log.i(TAG, "getDistanceMiles");

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(dest.latitude - origin.latitude);
        double lonDistance = Math.toRadians(dest.longitude - origin.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(origin.latitude)) * Math.cos(Math.toRadians(dest.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c / 1.609; // converts to miles

        return distance;
    }

    public SQLiteDatabase getDb(boolean writeable) {
        Log.i(TAG, "getDb");

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
