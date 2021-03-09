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

    // useful for verifying correct geographic sorting and filtering
    public static Store target = new Store(
            "Target",
            "some address",
            37.350170761350526, -121.96088796957218);

    public static Store wholeFoods = new Store(
            "Whole Foods",
            "some address",
            37.32351469164324, -122.03984393705552);

    public static Store traderJoes = new Store(
            "Trader Joe's",
            "some address",
            37.36787141649834, -122.03588330586433);

    public static Store walmart = new Store(
            "Walmart",
            "3255 Mission College Blvd, Santa Clara, CA 95054",
            37.390084, -121.9876223);

    public static Store homedepot = new Store(
            "The Home Depot",
            "2435 Lafayette St, Santa Clara, CA 95050",
            37.3655875, -121.9509001);

    public static Store safeway = new Store(
            "Safeway",
            "2760 Homestead Rd, Santa Clara, CA 95051",
            37.3380751,-121.9925121);

    public static Store costco = new Store(
            "Costco Wholesale",
            "1601 Coleman Ave, Santa Clara, CA 95050",
            37.3570353,-121.9415003);

    // Useful for debug testing
    public static Store kharkov = new Store(
            "Kharkov",
            "The Ukraine",
            49.978120510315215, 36.22342206859459);

    public static Map<String, Store> keywordToStoreMap = new HashMap<>();
    static {
        keywordToStoreMap.put("fish", traderJoes);
        keywordToStoreMap.put("cheese", wholeFoods);
        keywordToStoreMap.put("soap", target);
        keywordToStoreMap.put("lamp", target);
        keywordToStoreMap.put("pan", target);
        keywordToStoreMap.put("pans", target);
        keywordToStoreMap.put("blender", target);
        keywordToStoreMap.put("toaster", target);
        keywordToStoreMap.put("forks", target);
        keywordToStoreMap.put("knives", target);
        keywordToStoreMap.put("spoons", target);
        keywordToStoreMap.put("fork", target);
        keywordToStoreMap.put("knife", target);
        keywordToStoreMap.put("spoon", target);
        keywordToStoreMap.put("tide pods", target);
        keywordToStoreMap.put("legos", target);
        keywordToStoreMap.put("lego", target);
        keywordToStoreMap.put("camera", target);

        // Walmart
        keywordToStoreMap.put("pool", walmart);
        keywordToStoreMap.put("table", walmart);
        keywordToStoreMap.put("bow", walmart);

        // Home Depot
        keywordToStoreMap.put("shovel", homedepot);
        keywordToStoreMap.put("flower", homedepot);
        keywordToStoreMap.put("flowers", homedepot);
        keywordToStoreMap.put("soil", homedepot);
        keywordToStoreMap.put("tree", homedepot);
        keywordToStoreMap.put("trees", homedepot);
        keywordToStoreMap.put("seed", homedepot);
        keywordToStoreMap.put("seeds", homedepot);

        // Safeway
        keywordToStoreMap.put("apple", safeway);
        keywordToStoreMap.put("vinegar", safeway);
        keywordToStoreMap.put("chips", safeway);

        // Costco
        keywordToStoreMap.put("hotdog", costco);
        keywordToStoreMap.put("hotdogs", costco);
        keywordToStoreMap.put("pizza", costco);
        keywordToStoreMap.put("pizzas", costco);
        keywordToStoreMap.put("chicken bake", costco);
        keywordToStoreMap.put("olive oil", costco);
        keywordToStoreMap.put("lettuce", costco);
        keywordToStoreMap.put("rice", costco);
        keywordToStoreMap.put("avocado", costco);
        keywordToStoreMap.put("avocados", costco);
        keywordToStoreMap.put("tomatoes", costco);
        keywordToStoreMap.put("grapes", costco);
        keywordToStoreMap.put("chicken", costco);
        keywordToStoreMap.put("beef", costco);
        keywordToStoreMap.put("pork", costco);
        keywordToStoreMap.put("ribs", costco);
        keywordToStoreMap.put("lobster", costco);
        keywordToStoreMap.put("lamb", costco);

        // Debug
        keywordToStoreMap.put("soap", kharkov);
    }

    public static void populateDBWithTestData(SQLiteDatabase db) {
        for (Map.Entry<String, Store> entry : keywordToStoreMap.entrySet()) {
            StoreInfoDBHelper.addToDb(db, entry.getKey(), entry.getValue());
        }
    }
}
