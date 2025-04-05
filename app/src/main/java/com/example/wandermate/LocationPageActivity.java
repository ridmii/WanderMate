package com.example.wandermate;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPageActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String locationName;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);

        // Retrieve location data from intent
        Intent intent = getIntent();
        if (intent != null) {
            locationName = intent.getStringExtra("LOCATION_NAME");
            latitude = intent.getDoubleExtra("LOCATION_LAT", 0);
            longitude = intent.getDoubleExtra("LOCATION_LNG", 0);
        }

        // Load the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set the selected location on the map
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(locationName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }
}
