package com.example.androidnotification;

public class User {

    private String userId;
    private String email;
    private String token;

    public User() {
    }

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public User(String userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }


}
