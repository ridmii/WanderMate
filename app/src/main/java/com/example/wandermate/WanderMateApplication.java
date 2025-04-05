package com.example.wandermate;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class WanderMateApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}