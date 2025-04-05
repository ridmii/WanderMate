package com.example.wandermate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.content.res.ColorStateList;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat themeSwitch;
    private BottomNavigationView bottomNavigation;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.top_app_bar);
        setSupportActionBar(toolbar);

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }

        // Initialize theme switch
        themeSwitch = findViewById(R.id.darkModeSwitch);

        // Set the initial state based on saved preferences
        boolean savedDarkMode = getSharedPreferences("app_preferences", MODE_PRIVATE)
                .getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(savedDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);
        themeSwitch.setChecked(savedDarkMode);

        // Set up the listener for theme changes
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("app_preferences", MODE_PRIVATE)
                    .edit()
                    .putBoolean("dark_mode", isChecked)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            updateBottomNavTheme();
        });

        updateBottomNavTheme();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            loadFragment(new HomeFragment());
            return true;
        } else if (itemId == R.id.navigation_location) {
            // Launch LocationSearchActivity instead of loading fragment
            startActivity(new Intent(this, LocationSearchActivity.class));
            return true;
        } else if (itemId == R.id.navigation_weather) {
            loadFragment(new WeatherFragment());
            return true;
        } else if (itemId == R.id.navigation_profile) {
            loadFragment(new ProfileFragment());
            return true;
        }

        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void updateBottomNavTheme() {
        boolean isDarkMode = (getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES;

        int navBackground = isDarkMode ? R.color.darkNavBackground : R.color.lightNavBackground;
        int navItemColor = isDarkMode ? R.color.darkNavItemColor : R.color.lightNavItemColor;

        bottomNavigation.setBackgroundColor(getResources().getColor(navBackground));

        ColorStateList navItemColorStateList = ColorStateList.valueOf(getResources().getColor(navItemColor));
        bottomNavigation.setItemIconTintList(navItemColorStateList);
        bottomNavigation.setItemTextColor(navItemColorStateList);
    }
}