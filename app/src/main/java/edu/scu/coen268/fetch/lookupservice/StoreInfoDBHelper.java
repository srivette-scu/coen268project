package edu.scu.coen268.fetch.lookupservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StoreInfoDBHelper extends SQLiteOpenHelper {
    private static String TAG = "StoreInfoDBHelper";

    private static final String DB_NAME = "store_info.db";
    private static final int DB_VERSION = 1;

    public static final String CREATE_TABLE = "CREATE TABLE "+
            Schemas.StoreTable.TABLE_NAME + "(" +
            Schemas.StoreTable.KEYWORD + " TEXT NOT NULL, " +
            Schemas.StoreTable.STORE_NAME + " TEXT NOT NULL, " +
            Schemas.StoreTable.ADDRESS + " TEXT NOT NULL, " +
            Schemas.StoreTable.LATITUDE + " REAL NOT NULL, " +
            Schemas.StoreTable.LONGITUDE + " REAL NOT NULL" + ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Schemas.StoreTable.TABLE_NAME + ";";

    public StoreInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public static long addToDb(SQLiteDatabase db, String keyword, Store store) {
        Log.i(TAG, "addToDbStructured");

        ContentValues contentValues = new ContentValues();

        contentValues.put(Schemas.StoreTable.KEYWORD, keyword);
        contentValues.put(Schemas.StoreTable.STORE_NAME, store.getName());
        contentValues.put(Schemas.StoreTable.ADDRESS, store.getAddress());
        contentValues.put(Schemas.StoreTable.LATITUDE, store.getLatLng().latitude);
        contentValues.put(Schemas.StoreTable.LONGITUDE, store.getLatLng().longitude);

        long recordId = db.insert(Schemas.StoreTable.TABLE_NAME, null, contentValues);
        db.close();

        return recordId;
    }

    public static List<Store> getStoresForKeyword(SQLiteDatabase db, String keyword) {
        Log.i(TAG, "getStoresForKeyword");

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
}
