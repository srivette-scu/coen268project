package edu.scu.coen268.fetch.lookupservice;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LookupService extends Service {
    String TAG = this.getClass().getCanonicalName();

    public static double SCU_LAT = 37.3507689;
    public static double SCU_LONG = -121.9398422;
    public static double ONE_MILE = 1;

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

        // start DB
        // populate DB

        return START_NOT_STICKY;
    }

    public Set<Store> getStoresForItemListDummy(List<String> items) {
        Set<Store> stores = new HashSet<>();
        stores.add(new Store(
                "Target",
                "some address",
                37.350170761350526,
                -121.96088796957218));
        stores.add(new Store(
                "Trader Joe's",
                "some address",
                37.36787141649834,
                -122.03588330586433));
        stores.add(new Store(
                "Whole Foods",
                "some address",
                37.32351469164324,
                -122.03984393705552));

        return stores;
    }

    public Set<Store> getStoresForItemList(List<String> items) {
        Log.i(TAG, "getStoresForItemList");

        Set<Store> stores = new HashSet<>();
        for (String item : items) {
            List<Store> foundStores = getStoresForKeyword(item);
            if (!foundStores.isEmpty()) {
                // TODO get better lat/long
                stores.add(pickClosestStoreInRange(foundStores, SCU_LAT, SCU_LONG, ONE_MILE));
            }
        }

        return stores;
    }

    public void addToDb(String keyword, String name, String address, double latitude, double longitude) {
        Log.i(TAG, "addToDbRaw");

        Store store = new Store(name, address, latitude, longitude);
        addToDb(keyword, store);
    }

    public void addToDb(String keyword, Store store) {
        Log.i(TAG, "addToDbStructured");

        StoreInfoDBHelper dbHelper = new StoreInfoDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Schemas.StoreTable.KEYWORD, keyword);
        contentValues.put(Schemas.StoreTable.STORE_NAME, store.getName());
        contentValues.put(Schemas.StoreTable.ADDRESS, store.getAddress());
        contentValues.put(Schemas.StoreTable.LATITUDE, store.getLatLng().latitude);
        contentValues.put(Schemas.StoreTable.LONGITUDE, store.getLatLng().longitude);

        long recordId = db.insert(Schemas.StoreTable.TABLE_NAME, null, contentValues);
        db.close();
    }

    public List<Store> getStoresForKeyword(String keyword) {
        Log.i(TAG, "getStoresForKeyword");

        StoreInfoDBHelper dbHelper = new StoreInfoDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Store> stores = new ArrayList<>();

        String[] collumns = {
                Schemas.StoreTable.STORE_NAME,
                Schemas.StoreTable.ADDRESS,
                Schemas.StoreTable.LATITUDE,
                Schemas.StoreTable.LONGITUDE};
        String selection = Schemas.StoreTable.KEYWORD + " LIKE? ";
        String[] selectionArgs = {"%" + keyword + "%"};

        Cursor cursor = db.query(
                Schemas.StoreTable.TABLE_NAME,
                collumns,
                selection,
                selectionArgs,
                null,
                null,
                Schemas.StoreTable.STORE_NAME);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(Schemas.StoreTable.STORE_NAME));
            String address = cursor.getString(cursor.getColumnIndex(Schemas.StoreTable.ADDRESS));
            double latitude = cursor.getDouble(cursor.getColumnIndex(Schemas.StoreTable.LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(Schemas.StoreTable.LONGITUDE));

            stores.add(new Store(name, address, latitude, longitude));
        }

        if (stores.isEmpty()) {
            Log.i(TAG, "No stores found for keyword: " + keyword);
        }

        return stores;
    }

    public Store pickClosestStoreInRange(
            List<Store> stores,
            double currentLatitude,
            double currentLongitude,
            double rangeInMiles) {
        Log.i(TAG, "pickClosestStoreInRange");

        // TODO crappy radius search
        return stores.get(0);
    }

    // Utilities //
    public class LookupServiceBinder extends Binder {
        public LookupService getService() {
            return LookupService.this;
        }
    }
}
