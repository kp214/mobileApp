package com.example.mobileapp;

public class User {
    public String username;
    public String email;
    public String password;
    public static int ID = 1111;

    public User() {
        username = "";
        email = "";
        password = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        ID++;
    }

    public void updateUser(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getID() { return ID; }
}
