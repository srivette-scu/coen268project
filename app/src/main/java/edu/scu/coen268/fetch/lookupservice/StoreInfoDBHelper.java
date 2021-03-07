package edu.scu.coen268.fetch.lookupservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreInfoDBHelper extends SQLiteOpenHelper {
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
}
