package com.example.dione.noticesapp.api.interfaces;

import com.example.dione.noticesapp.api.models.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dione on 11/08/2016.
 */
public interface IWeather {
    @GET("{latitude},{longitude}")
    Call<Weather> getWeather(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
