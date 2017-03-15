package com.example.dione.noticesapp.event;

/**
 * Created by Donds on 3/15/2017.
 */

public class LoginRequestEvent {
    private String type; //admin or normal
    private String username;
    private String password;

    public LoginRequestEvent(String type, String username, String password) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
