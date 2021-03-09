package edu.scu.coen268.fetch.lookupservice;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public final class TestData {
    private static String TAG = "TestData";

    private TestData() {
        // Do not instantiate
    }

    public static LatLng scuLatLng = new LatLng(37.3507689, -121.9398422);

    public static Store target = new Store(
            "Target",
            "some address",
            37.350170761350526,
            -121.96088796957218);

    public static Store wholeFoods = new Store(
            "Whole Foods",
            "some address",
            37.32351469164324,
            -122.03984393705552);

    public static Store traderJoes = new Store(
            "Trader Joe's",
            "some address",
            37.36787141649834,
            -122.03588330586433);

    // useful for verifying correct geographic sorting and filtering
    public static Store kharkov = new Store(
            "Kharkov",
            "The Ukraine",
            49.978120510315215,
            36.22342206859459);

    public static Map<String, Store> keywordToStoreMap = new HashMap<>();
    static {
        // Trader Joe's
        keywordToStoreMap.put("fish", traderJoes);

        // Whole Foods
        keywordToStoreMap.put("cheese", wholeFoods);

        // Target
        keywordToStoreMap.put("soap", target);

        // Debug
        keywordToStoreMap.put("soap", kharkov);
    }

    public static void populateDBWithTestData(SQLiteDatabase db) {
        Log.i(TAG, "populateDbWithTestData");

        for (Map.Entry<String, Store> entry : keywordToStoreMap.entrySet()) {
            StoreInfoDBHelper.addToDb(db, entry.getKey(), entry.getValue());
        }
    }
}
