package com.example.wandermate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearbyPlacesFragment extends Fragment {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private ProgressBar progressBar;
    private PlacesClient placesClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesClient = Places.createClient(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        adapter = new PlacesAdapter(new ArrayList<>(), place -> {
            if (getActivity() instanceof PlacesAdapter.OnPlaceClickListener) {
                ((PlacesAdapter.OnPlaceClickListener) getActivity()).onPlaceClick(place);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            LatLng location = getArguments().getParcelable("location");
            if (location != null) {
                loadNearbyPlaces(location);
            }
        }

        return view;
    }

    private void loadNearbyPlaces(LatLng location) {
        progressBar.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
            return;
        }

        // Use FindCurrentPlaceRequest instead of SearchNearbyRequest
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.RATING,
                Place.Field.PHOTO_METADATAS
        );

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        placesClient.findCurrentPlace(request).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful() && task.getResult() != null) {
                FindCurrentPlaceResponse response = task.getResult();
                List<Place> places = new ArrayList<>();
                for (com.google.android.libraries.places.api.model.PlaceLikelihood likelihood : response.getPlaceLikelihoods()) {
                    places.add(likelihood.getPlace());
                }
                adapter.updatePlaces(places);
            } else {
                Toast.makeText(getContext(), "Failed to load nearby places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getArguments() != null) {
                    LatLng location = getArguments().getParcelable("location");
                    if (location != null) {
                        loadNearbyPlaces(location);
                    }
                }
            } else {
                Toast.makeText(getContext(), "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}