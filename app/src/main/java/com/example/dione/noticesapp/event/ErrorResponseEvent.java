package com.example.dione.noticesapp.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Donds on 3/15/2017.
 */

public class ErrorResponseEvent {

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
