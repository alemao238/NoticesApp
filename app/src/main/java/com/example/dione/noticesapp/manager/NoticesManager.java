package com.example.dione.noticesapp.manager;

import android.content.Context;

import com.example.dione.noticesapp.api.NoticesClient;
import com.example.dione.noticesapp.api.interfaces.IWeather;
import com.example.dione.noticesapp.api.models.Weather;
import com.example.dione.noticesapp.event.GetWeatherEvent;
import com.example.dione.noticesapp.event.SendWeatherEvent;
import com.example.dione.noticesapp.event.SendWeatherEventError;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dione on 11/08/2016.
 */
public class NoticesManager {
    private Context mContext;
    private Bus mBus;
    private NoticesClient sNoticesClient;
    public NoticesManager(Context context, Bus bus) {
        this.mContext = context;
        this.mBus = bus;
        sNoticesClient = NoticesClient.getClient();
    }

    @Subscribe
    public void onGetWeatherEvent(GetWeatherEvent getWeatherEvent) {
        IWeather iWeather = NoticesClient.mRestAdapter.create(IWeather.class);
        Call<Weather> weatherCall = iWeather.getWeather(String.valueOf(getWeatherEvent.getLatitude()), String.valueOf(getWeatherEvent.getLongitude()));
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()){
                    mBus.post(new SendWeatherEvent(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                mBus.post(new SendWeatherEventError(t.getLocalizedMessage()));
            }
        });
    }
}
