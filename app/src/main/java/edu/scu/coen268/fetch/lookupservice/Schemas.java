package edu.scu.coen268.fetch.lookupservice;

import android.provider.BaseColumns;

public class Schemas {

    private Schemas() {
        // Do not instantiate
    }

    public class StoreTable implements BaseColumns {
        public static final String TABLE_NAME = "StoreData";
        public static final String KEYWORD = "keyword";
        public static final String STORE_NAME = "store_name";
        public static final String ADDRESS = "address";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}
