package com.example.dione.noticesapp.event;

/**
 * Created by Donds on 3/15/2017.
 */

public class RegisterRequestEvent {
    private String email;
    private String nickname;
    private String password;

    public RegisterRequestEvent(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
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
}
