package com.example.wandermate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private PlacesClient placesClient;
    private String placeId;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Check for direct coordinates first
        if (getIntent().hasExtra("LAT_LNG")) {
            LatLng latLng = getIntent().getParcelableExtra("LAT_LNG");
            placeName = getIntent().getStringExtra("PLACE_NAME");
        } else {
            // Existing code for place ID lookup
            placeId = getIntent().getStringExtra("PLACE_ID");
            placeName = getIntent().getStringExtra("PLACE_NAME");
        }

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_places_api_key));
        }
        placesClient = Places.createClient(this);

        // Directly show location if coordinates are available
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (getIntent().hasExtra("LAT_LNG")) {
            LatLng latLng = getIntent().getParcelableExtra("LAT_LNG");
            showLocationOnMap(latLng, placeName);
        } else if (placeId != null && !placeId.isEmpty()) {
            fetchPlaceDetails(placeId);
        } else {
            showDefaultLocation();
        }

        // Check if map is available
        if (mMap == null) {
            Toast.makeText(this, "Google Maps not available", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Enable basic map features
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Check location permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing map", e);
            Toast.makeText(this, "Map initialization failed", Toast.LENGTH_LONG).show();
        }
    }

    private void showLocationOnMap(LatLng latLng, String placeName) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(placeName));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    LatLng latLng = place.getLatLng();

                    if (latLng != null) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(place.getName())
                                .snippet(place.getAddress()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    } else {
                        showDefaultLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Place not found: " + e.getMessage());
                    Toast.makeText(this, "Couldn't find location details", Toast.LENGTH_SHORT).show();
                    showDefaultLocation();
                });
    }

    private void showDefaultLocation() {
        LatLng defaultLocation = new LatLng(40.7128, -74.0060); // New York
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Default Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mMap != null) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
            }
        }
    }
}