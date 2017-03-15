package com.example.dione.noticesapp.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Donds on 3/15/2017.
 */

public class RegisterTokenResponseEvent {
    @SerializedName("status")
    @Expose
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
