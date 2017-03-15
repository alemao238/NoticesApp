package com.example.dione.noticesapp.api;

import com.example.dione.noticesapp.utilities.ApplicationConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dione on 11/08/2016.
 */
public class NoticesClient {
    private static final String BASE_URL = ApplicationConstants.API_BASE_URL;

    private static NoticesClient mNoticesClient;
    public static Retrofit mRestAdapter;

    public static NoticesClient getClient() {
        if (mNoticesClient == null)
            mNoticesClient = new NoticesClient();
        return mNoticesClient;
    }

    private NoticesClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        mRestAdapter = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}
