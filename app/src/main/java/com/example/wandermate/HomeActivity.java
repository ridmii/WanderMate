package com.example.wandermate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreate: Starting HomeActivity");
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate: Setting content view");
            setContentView(R.layout.activity_home);
            Log.d(TAG, "onCreate: Content view set successfully");

            // Hide the action bar
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // Initialize bottom navigation
            Log.d(TAG, "onCreate: Finding bottom navigation view");
            bottomNavigationView = findViewById(R.id.bottom_navigation);

            if (bottomNavigationView == null) {
                Log.e(TAG, "onCreate: Bottom navigation view not found!");
                Toast.makeText(this, "Error: Layout issue detected", Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "onCreate: Setting up navigation listener");
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Log.d(TAG, "Navigation item selected: " + itemId);

                try {
                    if (itemId == R.id.navigation_home) {
                        loadFragment(new HomeFragment());
                        return true;
                    } else if (itemId == R.id.navigation_location) {
                        // Launch LocationSearchActivity instead of loading fragment
                        startActivity(new Intent(HomeActivity.this, LocationSearchActivity.class));
                        return true;
                    } else if (itemId == R.id.navigation_weather) {
                        loadFragment(new WeatherFragment());
                        return true;
                    } else if (itemId == R.id.navigation_profile) {
                        loadFragment(new ProfileFragment());
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error handling navigation", e);
                    Toast.makeText(this, "Error loading section", Toast.LENGTH_SHORT).show();
                }

                return false;
            });

            // Set default fragment
            Log.d(TAG, "onCreate: Loading default fragment");
            loadFragment(new HomeFragment());
            Log.d(TAG, "onCreate: HomeActivity setup complete");

        } catch (Exception e) {
            Log.e(TAG, "Fatal error in HomeActivity onCreate", e);
            Toast.makeText(this, "An error occurred while starting the app", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        try {
            Log.d(TAG, "loadFragment: Loading " + fragment.getClass().getSimpleName());
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            Log.d(TAG, "loadFragment: Fragment loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in loadFragment", e);
        }
    }
}