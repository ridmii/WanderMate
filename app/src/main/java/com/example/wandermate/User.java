package com.example.wandermate;

public class User {
    private String name;
    private String email;

    // Default constructor for Firebase
    public User() {}

    // Constructor to initialize user data
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters (optional if you need them)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
