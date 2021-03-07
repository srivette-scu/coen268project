package edu.scu.coen268.fetch.lookupservice;

public class Store {
    String TAG = this.getClass().getCanonicalName();

    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;

    public Store(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
