package com.example.wandermate;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.imageview.ShapeableImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvEmail;
    private CircleImageView profileImage;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        profileImage = view.findViewById(R.id.profileImage);

        // Initialize SharedPreferences
        preferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // Load user data
        loadUserData();

        // Set up click listeners for profile options
        LinearLayout llEditProfile = view.findViewById(R.id.llEditProfile);
        LinearLayout llSettings = view.findViewById(R.id.llSettings);
        LinearLayout llLogout = view.findViewById(R.id.llLogout);

        llEditProfile.setOnClickListener(v -> {
            // Navigate to edit profile screen
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to EditProfileActivity
        });

        llSettings.setOnClickListener(v -> {
            // Navigate to settings screen
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to SettingsActivity
        });

        llLogout.setOnClickListener(v -> {
            // Handle logout
            logoutUser();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh user data when fragment becomes visible
        loadUserData();
    }

    private void loadUserData() {
        // Get user data from SharedPreferences
        String username = preferences.getString("username", "Username");
        String email = preferences.getString("email", "email@example.com");

        // Update UI with user data
        tvUsername.setText(username);
        tvEmail.setText(email);

        // You can also load profile image from storage or use placeholder
        // For now, we're using the default image from drawable
        // If you have user profile image in storage, load it here
        // Example:
        // String imagePath = preferences.getString("profile_image", "");
        // if (!TextUtils.isEmpty(imagePath)) {
        //     Glide.with(this).load(imagePath).into(profileImage);
        // }
    }

    private void logoutUser() {
        // Clear all user data from SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity and clear the back stack
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Show logout confirmation
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
}