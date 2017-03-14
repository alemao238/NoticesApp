package com.example.dione.noticesapp.modules.models;

import android.app.ProgressDialog;

/**
 * Created by Donds on 3/13/2017.
 */

public class NoticesModel {
    private String shortMessage;
    private String user;
    private String message;
    private String date;
    private String title;
    public NoticesModel() {
    }

    public NoticesModel(String shortMessage, String user, String message, String date, String title) {
        this.user = user;
        this.message = message;
        this.date = date;
        this.title = title;
        this.shortMessage = shortMessage;
    }


    public String getUser() {
        return user;
    }
    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortMessage() {
        return shortMessage;
    }
    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }
}
