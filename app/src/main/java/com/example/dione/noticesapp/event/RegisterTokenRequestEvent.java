package com.example.dione.noticesapp.event;

/**
 * Created by Donds on 3/15/2017.
 */

public class RegisterTokenRequestEvent {
    private String token;
    private String username;
    private String type;
    private String uid;
    public RegisterTokenRequestEvent(String token, String username,String uid, String type) {
        this.token = token;
        this.username = username;
        this.type = type;
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }
}
