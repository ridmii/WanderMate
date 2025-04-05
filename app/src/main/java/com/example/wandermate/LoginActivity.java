package com.example.wandermate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerRedirect;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerRedirect = findViewById(R.id.registerRedirect);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.contains("email") && mAuth.getCurrentUser() != null) {
            redirectToHomeActivity();
        }

        // Set up click listeners
        loginButton.setOnClickListener(v -> {
            // FOR TESTING PURPOSES ONLY - BYPASS LOGIN
            bypassLoginAndRedirect();

            // Original login logic (commented out for testing)
            // loginUser();
        });

        registerRedirect.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    /**
     * TESTING ONLY - Bypasses authentication and redirects to HomeActivity
     */
    private void bypassLoginAndRedirect() {
        // Set test user data in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", "test@wandermate.com");
        editor.putString("username", "TestUser");
        editor.apply();

        // Redirect to HomeActivity
        redirectToHomeActivity();
        Toast.makeText(this, "TEST MODE: Bypassing login", Toast.LENGTH_SHORT).show();
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password cannot be empty");
            return;
        }

        // Authenticate with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save user data to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("username", email.split("@")[0]);
                        editor.apply();

                        // Redirect to main activity
                        redirectToHomeActivity();
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}