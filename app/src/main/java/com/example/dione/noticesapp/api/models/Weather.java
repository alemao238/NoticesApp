package com.example.dione.noticesapp.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dione on 11/08/2016.
 */
public class Weather {
    @SerializedName("currently")
    private Currently mCurrently;
    public Currently getCurrently() {
        return mCurrently;
    }

}
