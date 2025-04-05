package com.example.wandermate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etEmail;
    private ImageView ivProfileImage;
    private Button btnSave;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private StorageReference storageRef;

    private Uri imageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnSave = findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile...");

        ivProfileImage.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(v -> saveUserProfile());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivProfileImage.setImageURI(imageUri);
        }
    }

    private void saveUserProfile() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        if (imageUri != null) {
            StorageReference fileRef = storageRef.child(user.getUid() + ".jpg");
            fileRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> updateUserProfile(username, email, uri.toString()));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateUserProfile(username, email, null);
        }
    }

    private void updateUserProfile(String username, String email, String imageUrl) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", username);
        userData.put("email", email);
        if (imageUrl != null) {
            userData.put("profileImage", imageUrl);
        }

        userRef.updateChildren(userData).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
