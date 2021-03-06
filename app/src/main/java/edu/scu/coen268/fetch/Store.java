package edu.scu.coen268.fetch;

public class Store {
    String TAG = this.getClass().getCanonicalName();

    private final String location;
    private final String name;

    public Store(String location, String name) {
        this.location = location;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
