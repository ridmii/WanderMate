package com.example.wandermate;

public class Destination {
    private String name;
    private String location;
    private String price;
    private String placeId; // Optional if you use Google Places
    private double latitude; // Added latitude
    private double longitude; // Added longitude
    private int imageResource;

    public Destination(String name, String location, String price, String placeId, double latitude, double longitude, int imageResource) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.placeId = placeId;
        this.latitude = latitude; // Assign latitude
        this.longitude = longitude; // Assign longitude
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getLatitude() {
        return latitude; // Added getter for latitude
    }

    public double getLongitude() {
        return longitude; // Added getter for longitude
    }

    public int getImageResource() {
        return imageResource; // Added getter for image resource
    }
}
