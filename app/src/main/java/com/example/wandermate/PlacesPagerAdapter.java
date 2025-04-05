package com.example.wandermate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.gms.maps.model.LatLng;

public class PlacesPagerAdapter extends FragmentStateAdapter {
    private LatLng currentLocation;

    public PlacesPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    public void setLocation(LatLng location) {
        this.currentLocation = location;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle args = new Bundle();

        if (currentLocation != null) {
            args.putParcelable("location", currentLocation);
        }

        switch(position) {
            case 0:
                fragment = new NearbyPlacesFragment();
                break;
            case 1:
                fragment = new PopularPlacesFragment();
                break;
            default:
                throw new IllegalArgumentException();
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}