package com.example.wandermate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

public class LocationSearchActivity extends AppCompatActivity
        implements OnMapReadyCallback, PlacesAdapter.OnPlaceClickListener {

    private GoogleMap mMap;
    private LinearLayout bottomSheet;
    private ViewPager2 viewPager;
    private PlacesPagerAdapter pagerAdapter;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_places_api_key));
        }

        // Setup UI Components
        setupSearchBar();
        setupBottomSheet();
        setupBottomNavigation();
    }

    private void setupSearchBar() {
        ImageButton clearSearchBtn = findViewById(R.id.clear_search_btn);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Configure Autocomplete
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
        ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                handlePlaceSelection(place);
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle error
            }
        });

        clearSearchBtn.setOnClickListener(v -> {
            autocompleteFragment.setText("");
            bottomSheet.setVisibility(View.GONE);
            hideKeyboard();
        });
    }

    private void handlePlaceSelection(Place place) {
        if (place.getLatLng() != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            bottomSheet.setVisibility(View.VISIBLE);
            pagerAdapter.setLocation(place.getLatLng());
            hideKeyboard();
        }
    }

    private void setupBottomSheet() {
        bottomSheet = findViewById(R.id.bottom_sheet);
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);

        pagerAdapter = new PlacesPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Nearby" : position == 1 ? "Popular" : "History");
        }).attach();
    }

    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34.0, 151.0), 10));
    }

    @Override
    public void onPlaceClick(Place place) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
    }
}