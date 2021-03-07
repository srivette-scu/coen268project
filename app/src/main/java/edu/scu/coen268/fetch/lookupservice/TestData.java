package edu.scu.coen268.fetch.lookupservice;

import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public final class TestData {
    private TestData() {
        // Do not instantiate
    }

    public static LatLng scuLatLng = new LatLng(37.3507689, -121.9398422);
    public static double bikingDistance = 4;

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

    public static Map<String, Store> keywordToStoreMap = new HashMap<>();
    static {
        keywordToStoreMap.put("fish", traderJoes);
        keywordToStoreMap.put("cheese", wholeFoods);
        keywordToStoreMap.put("soap", target);
    }

    public static void populateDBWithTestData(SQLiteDatabase db) {
        for (Map.Entry<String, Store> entry : keywordToStoreMap.entrySet()) {
            StoreInfoDBHelper.addToDb(db, entry.getKey(), entry.getValue());
        }
    }
}
