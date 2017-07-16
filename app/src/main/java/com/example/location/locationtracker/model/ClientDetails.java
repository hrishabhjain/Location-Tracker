package com.example.location.locationtracker.model;

import javax.inject.Inject;

import okhttp3.Credentials;

/**
 * Created by Rishabh on 15/07/17.
 */

public class ClientDetails {
    private String username;
    private String password;

    @Inject
    public ClientDetails() {
        username = "your_username";
        password = "your_password";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
