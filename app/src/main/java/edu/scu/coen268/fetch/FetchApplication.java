package edu.scu.coen268.fetch;

import android.app.Application;

public class FetchApplication extends Application {
    private double searchRadiusMiles = 20;

    public double getSearchRadiusMiles() {
        return searchRadiusMiles;
    }

    public void setSearchRadiusMiles(double searchRadiusMiles) {
        this.searchRadiusMiles = searchRadiusMiles;
    }
}
