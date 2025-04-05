package com.example.wandermate;

import com.google.android.gms.maps.model.LatLng;

public class PlaceItem {
    private String id;
    private String name;
    private String address;
    private LatLng latLng;

    // Constructor with all fields
    public PlaceItem(String id, String name, String address, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    // New constructor for cases where we don't have coordinates
    public PlaceItem(String id, String name, String address) {
        this(id, name, address, null);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public LatLng getLatLng() { return latLng; }
}