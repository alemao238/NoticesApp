package com.example.dione.noticesapp.event;


//import retrofit.RetrofitError;

/**
 * Created by dione on 11/08/2016.
 */
public class SendWeatherEventError {
    private String mRetroFitError;
    public SendWeatherEventError(String error){
        this.mRetroFitError = error;
    }

    public String getmRetroFitError() {
        return mRetroFitError;
    }
}
