package edu.scu.coen268.fetch.lookupservice;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class Store {
    private final String name;
    private final String address;
    private final LatLng latLng;

    public Store(String name, String address, double latitude, double longitude) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(address));
        Preconditions.checkNotNull(latitude);
        Preconditions.checkNotNull(longitude);

        this.name = name;
        this.address = address;
        this.latLng = new LatLng(latitude, longitude);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
