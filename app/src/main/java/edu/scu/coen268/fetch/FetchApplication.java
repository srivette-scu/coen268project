package edu.scu.coen268.fetch;

import android.app.Application;

public class FetchApplication extends Application {
    private double searchRadiusMiles = 20;
    private boolean isDemo = true;

    public double getSearchRadiusMiles() {
        return searchRadiusMiles;
    }

    public void setSearchRadiusMiles(double searchRadiusMiles) {
        this.searchRadiusMiles = searchRadiusMiles;
    }

    public boolean isDemo() {
        return isDemo;
    }
}
