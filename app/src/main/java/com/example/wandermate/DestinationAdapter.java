package com.example.wandermate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private List<Destination> destinations;
    private OnDestinationClickListener listener;

    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    public DestinationAdapter(List<Destination> destinations, OnDestinationClickListener listener) {
        this.destinations = destinations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.nameTextView.setText(destination.getName());
        holder.locationTextView.setText(destination.getLocation());
        holder.priceTextView.setText(destination.getPrice());
        holder.imageView.setImageResource(destination.getImageResource()); // Set the image resource

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDestinationClick(destination);
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView locationTextView;
        TextView priceTextView;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_destination);
            nameTextView = itemView.findViewById(R.id.tv_destination_name);
            locationTextView = itemView.findViewById(R.id.tv_destination_location);
            priceTextView = itemView.findViewById(R.id.tv_destination_price);
        }
    }
}
