package com.example.wandermate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder> {

    private List<PlaceItem> placesList;
    private final OnPlaceClickListener clickListener;

    public interface OnPlaceClickListener {
        void onPlaceClick(PlaceItem place);
    }

    public NearbyPlacesAdapter(List<PlaceItem> placesList, OnPlaceClickListener clickListener) {
        this.placesList = placesList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceItem place = placesList.get(position);
        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getAddress());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPlaceClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.place_name);
            placeAddress = itemView.findViewById(R.id.place_address);
        }
    }
}