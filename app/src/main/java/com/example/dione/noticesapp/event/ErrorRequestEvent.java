package com.example.dione.noticesapp.event;

/**
 * Created by Donds on 3/15/2017.
 */

public class ErrorRequestEvent {
    private String pErrorMessage;
    public ErrorRequestEvent(String errorMessage){
        this.pErrorMessage = errorMessage;
    }

    public String getpErrorMessage() {
        return pErrorMessage;
    }
}
