package com.example.wandermate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private RecyclerView categoryRecyclerView;
    private RecyclerView popularDestinationsRecyclerView;
    private TextView locationTextView;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            Log.d(TAG, "onCreateView: Starting to create HomeFragment view");
            view = inflater.inflate(R.layout.fragment_home, container, false);
            Log.d(TAG, "onCreateView: View inflated successfully");

            // Find views
            try {
                locationTextView = view.findViewById(R.id.tv_current_location);
                Log.d(TAG, "onCreateView: Location TextView found");
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Failed to find location TextView", e);
            }

            try {
                categoryRecyclerView = view.findViewById(R.id.rv_categories);
                Log.d(TAG, "onCreateView: Category RecyclerView found");
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Failed to find category RecyclerView", e);
            }

            try {
                popularDestinationsRecyclerView = view.findViewById(R.id.rv_popular_destinations);
                Log.d(TAG, "onCreateView: Popular destinations RecyclerView found");
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Failed to find popular destinations RecyclerView", e);
            }

            // Setup location client
            try {
                Context context = getContext();
                if (context != null) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                    Log.d(TAG, "onCreateView: Location client initialized");
                } else {
                    Log.e(TAG, "onCreateView: Context is null");
                }
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Failed to initialize location client", e);
            }

            // Setup features
            try {
                fetchCurrentLocation();
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Error fetching location", e);
            }

            try {
                setupCategoryRecyclerView();
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Error setting up category RecyclerView", e);
            }

            try {
                setupPopularDestinationsRecyclerView();
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Error setting up destinations RecyclerView", e);
            }

            Log.d(TAG, "onCreateView: HomeFragment view setup complete");
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Critical error creating HomeFragment view", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error loading home screen", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    private void fetchCurrentLocation() {
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "fetchCurrentLocation: Context is null");
            return;
        }

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "fetchCurrentLocation: Requesting location permissions");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Log.d(TAG, "fetchCurrentLocation: Getting last location");
        try {
            // Get the last known location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Update the TextView with the current location
                                String currentLocation = "📍 " + location.getLatitude() + ", " + location.getLongitude();
                                locationTextView.setText(currentLocation);
                                Log.d(TAG, "onSuccess: Location updated: " + currentLocation);
                            } else {
                                locationTextView.setText("Unable to fetch location");
                                Log.d(TAG, "onSuccess: Location is null");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error getting location", e);
                        locationTextView.setText("Location error");
                    });
        } catch (Exception e) {
            Log.e(TAG, "fetchCurrentLocation: Error", e);
            locationTextView.setText("Location service error");
        }
    }

    private void setupCategoryRecyclerView() {
        if (categoryRecyclerView == null || getContext() == null) {
            Log.e(TAG, "setupCategoryRecyclerView: RecyclerView or context is null");
            return;
        }

        try {
            List<Category> categories = new ArrayList<>();
            categories.add(new Category("🏔 Mountain", R.drawable.mountain));
            categories.add(new Category("🌊 Waterfall", R.drawable.waterfall));
            categories.add(new Category("🌿 Forest", R.drawable.forest));
            categories.add(new Category("🏖 Beach", R.drawable.b));
            categories.add(new Category("🏕 Camping", R.drawable.c));

            CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            categoryRecyclerView.setAdapter(categoryAdapter);
            Log.d(TAG, "setupCategoryRecyclerView: Categories set up successfully");
        } catch (Exception e) {
            Log.e(TAG, "setupCategoryRecyclerView: Error", e);
        }
    }

    private void setupPopularDestinationsRecyclerView() {
        if (popularDestinationsRecyclerView == null || getContext() == null) {
            Log.e(TAG, "setupPopularDestinationsRecyclerView: RecyclerView or context is null");
            return;
        }

        try {
            List<Destination> destinations = new ArrayList<>();
            destinations.add(new Destination("The Mannar Fort", "Mannar, Sri Lanka", "$20", "ChIJl0EGu5E5IjkR3u7O0T0N8XI", 8.9775, 79.9093, R.drawable.mannar));
            destinations.add(new Destination("Bromo Mountain", "Bali, Indonesia", "$50", "ChIJpQGFYcXU8jMRn77ZCvCKUHE", -7.9425, 112.9533, R.drawable.bali));
            destinations.add(new Destination("The Pink Beach", "Komodo Island, Indonesia", "$30", "ChIJPTw57X2m8jMRwZV-R2cNsS8", -8.6265, 119.5444, R.drawable.beach));
            destinations.add(new Destination("Lotus Tower", "Colombo, Sri Lanka", "$36", "ChIJPz6-wmbqIjkRrVFpHDBn2j8", 6.9271, 79.8612, R.drawable.lotus));

            DestinationAdapter destinationAdapter = new DestinationAdapter(destinations, this::onDestinationClick);
            popularDestinationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            popularDestinationsRecyclerView.setAdapter(destinationAdapter);
            Log.d(TAG, "setupPopularDestinationsRecyclerView: Destinations set up successfully");
        } catch (Exception e) {
            Log.e(TAG, "setupPopularDestinationsRecyclerView: Error", e);
        }
    }

    private void onDestinationClick(Destination destination) {
        try {
            Intent intent = new Intent(getActivity(), LocationPageActivity.class);
            intent.putExtra("LOCATION_NAME", destination.getName());
            intent.putExtra("LOCATION_PLACE", destination.getLocation());
            intent.putExtra("LOCATION_PRICE", destination.getPrice());
            intent.putExtra("LOCATION_LAT", destination.getLatitude());
            intent.putExtra("LOCATION_LNG", destination.getLongitude());
            intent.putExtra("LOCATION_IMAGE", destination.getImageResource());
            startActivity(intent);
            Log.d(TAG, "onDestinationClick: Started LocationPageActivity for " + destination.getName());
        } catch (Exception e) {
            Log.e(TAG, "onDestinationClick: Error starting LocationPageActivity", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error opening location details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}