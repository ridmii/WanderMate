package com.example.wandermate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PopularPlacesFragment extends Fragment implements PlacesAdapter.OnPlaceClickListener {

    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private ProgressBar progressBar;
    private TextView noPlacesText;
    private List<Place> popularPlaces = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        noPlacesText = view.findViewById(R.id.no_places_text);

        setupRecyclerView();
        loadPopularPlaces();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new PlacesAdapter(popularPlaces, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadPopularPlaces() {
        progressBar.setVisibility(View.VISIBLE);
        noPlacesText.setVisibility(View.GONE);

        new android.os.Handler().postDelayed(() -> {
            popularPlaces.clear();

            // Create mock Place objects instead of PlaceItem
            popularPlaces.add(createMockPlace("Eiffel Tower", "Paris, France", new LatLng(48.8584, 2.2945)));
            popularPlaces.add(createMockPlace("Colosseum", "Rome, Italy", new LatLng(41.8902, 12.4922)));
            popularPlaces.add(createMockPlace("Statue of Liberty", "New York, USA", new LatLng(40.6892, -74.0445)));
            popularPlaces.add(createMockPlace("Great Wall of China", "China", new LatLng(40.4319, 116.5704)));
            popularPlaces.add(createMockPlace("Taj Mahal", "Agra, India", new LatLng(27.1751, 78.0421)));

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

            if (popularPlaces.isEmpty()) {
                noPlacesText.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private Place createMockPlace(String name, String address, LatLng latLng) {
        return Place.builder()
                .setName(name)
                .setAddress(address)
                .setLatLng(latLng)
                .build();
    }

    @Override
    public void onPlaceClick(Place place) {
        if (getActivity() instanceof LocationSearchActivity) {
            ((LocationSearchActivity) getActivity()).onPlaceClick(place);
        }
    }
}