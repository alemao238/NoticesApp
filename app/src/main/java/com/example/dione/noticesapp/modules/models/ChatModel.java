package com.example.dione.noticesapp.modules.models;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Date;

/**
 * Created by Donds on 3/17/2017.
 */

public class ChatModel {
    private String message;
    private String from;
    private String locTime;

    public ChatModel(){};
    public ChatModel(String message, String from) {
        this.message = message;
        LocalTime localTime = new LocalTime();
        this.locTime = localTime.toString();
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public String getLocTime() {
        return locTime;
    }

    public String getFrom() {
        return from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime;
    }
}
