package com.example.wandermate.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    // Save user data
    public void saveUser(String userId, String name, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);

        databaseReference.child(userId).setValue(userMap);
    }
    // Update user details
    public void updateUser(String userId, String name) {
        databaseReference.child(userId).child("name").setValue(name);
    }

    // Delete user
    public void deleteUser(String userId) {
        databaseReference.child(userId).removeValue();
    }
}
