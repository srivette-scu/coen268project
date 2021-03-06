package edu.scu.coen268.fetch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

        // start DB
        // populate DB

        return START_NOT_STICKY;
    }

    public List<Store> getStoresForItemList(List<String> items) {
        Log.i(TAG, "getStoresForItemList");

        List<Store> stores = new ArrayList<>();
        stores.add(new Store("testLocation", "testName"));
        return stores;
    }

    class LookupServiceBinder extends Binder {
        public LookupService getService() {
            return LookupService.this;
        }
    }
}
