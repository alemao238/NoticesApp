package com.example.dione.noticesapp.api.interfaces;

import com.example.dione.noticesapp.api.models.Weather;
import com.example.dione.noticesapp.event.RegisterResponseEvent;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by dione on 11/08/2016.
 */
public interface IUsers {
    @POST("user/create")
    @FormUrlEncoded
    Call<RegisterResponseEvent> registerUser(@FieldMap Map<String, String> values);
}
