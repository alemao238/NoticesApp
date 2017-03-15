package com.example.dione.noticesapp.event;

/**
 * Created by Donds on 3/15/2017.
 */

public class RegisterRequestEvent {
    private String email;
    private String nickname;
    private String password;
    private String photoUrl;
    private String username;
    public RegisterRequestEvent(String email, String nickname, String password, String photoUrl, String username){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.photoUrl = photoUrl;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUsername() {
        return username;
    }
}
