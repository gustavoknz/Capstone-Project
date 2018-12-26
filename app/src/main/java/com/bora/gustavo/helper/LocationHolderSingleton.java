package com.bora.gustavo.helper;

import android.location.Location;

public class LocationHolderSingleton {
    private static final LocationHolderSingleton ourInstance = new LocationHolderSingleton();

    public static LocationHolderSingleton getInstance() {
        return ourInstance;
    }

    private Location location;

    private LocationHolderSingleton() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
