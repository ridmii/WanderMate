package com.example.wandermate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView placeName;
    private TextView placeAddress;
    private TextView placeDescription;
    private ImageView placeImage;
    private CardView mapCard;
    private GoogleMap mMap;
    private LatLng placeLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        // Initialize views
        placeName = findViewById(R.id.place_name);
        placeAddress = findViewById(R.id.place_address);
        placeDescription = findViewById(R.id.place_description);
        placeImage = findViewById(R.id.place_image);
        mapCard = findViewById(R.id.map_card);

        // Get data from intent
        String name = getIntent().getStringExtra("PLACE_NAME");
        String address = getIntent().getStringExtra("PLACE_ADDRESS");
        placeLatLng = getIntent().getParcelableExtra("LAT_LNG");

        // Set the data
        placeName.setText(name);
        placeAddress.setText(address);
        placeDescription.setText(getString(R.string.lorem_ipsum)); // Sample description

        // Load image (using placeholder for now)
        Glide.with(this)
                .load(R.drawable.tower)
                .into(placeImage);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (placeLatLng != null) {
            // Add marker and move camera
            mMap.addMarker(new MarkerOptions()
                    .position(placeLatLng)
                    .title(placeName.getText().toString()));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15f));

            // Customize map
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
        }
    }
}