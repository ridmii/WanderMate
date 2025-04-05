package com.example.wandermate.models;

public class User {
    public String name, email;

    public User() {}  // Default constructor for Firebase

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
