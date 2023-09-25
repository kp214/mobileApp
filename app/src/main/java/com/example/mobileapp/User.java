package com.example.mobileapp;

public class User {
    public String userName;
    public String email;
    public String password;
    public static int ID = 1111;

    public User() {
        userName = "";
        email = "";
        password = "";
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        ID++;
    }

    public int getID() { return ID; }
}
