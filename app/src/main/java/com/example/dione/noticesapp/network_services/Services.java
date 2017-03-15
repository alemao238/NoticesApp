package com.example.dione.noticesapp.network_services;

import com.example.dione.noticesapp.api.NoticesClient;
import com.example.dione.noticesapp.api.interfaces.IUsers;
import com.example.dione.noticesapp.application.NoticesApplication;
import com.example.dione.noticesapp.event.LoginRequestEvent;
import com.example.dione.noticesapp.event.LoginResponseEvent;
import com.example.dione.noticesapp.event.RegisterRequestEvent;
import com.example.dione.noticesapp.event.RegisterResponseEvent;
import com.example.dione.noticesapp.event.RegisterTokenRequestEvent;
import com.example.dione.noticesapp.event.RegisterTokenResponseEvent;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Donds on 3/15/2017.
 */

public class Services extends BaseService {
    public Services(NoticesApplication noticesApplication) {
        super(noticesApplication);
    }

    @Subscribe
    public void onRegisterEvent(RegisterRequestEvent registerRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> registerParams = new HashMap<>();
        registerParams.put("email", registerRequestEvent.getEmail());
        registerParams.put("name", registerRequestEvent.getNickname());
        registerParams.put("password", registerRequestEvent.getPassword());
        registerParams.put("username", registerRequestEvent.getUsername());
        registerParams.put("type", "normal");
        registerParams.put("photo", registerRequestEvent.getPhotoUrl());
        Call<RegisterResponseEvent> registerCall = iUsers.registerUser(registerParams);
        asyncRequest(registerCall);
    }

    @Subscribe
    public void onSendLoginEvent(LoginRequestEvent loginRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("type", loginRequestEvent.getType());
        loginParams.put("username", loginRequestEvent.getUsername());
        loginParams.put("password", loginRequestEvent.getPassword());

        Call<LoginResponseEvent> loginCall = iUsers.loginUser(loginParams);
        asyncRequest(loginCall);
    }

    @Subscribe
    public void onSendFcmToken(RegisterTokenRequestEvent registerTokenRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> fcmParams = new HashMap<>();
        fcmParams.put("type", registerTokenRequestEvent.getType());
        fcmParams.put("username", registerTokenRequestEvent.getUsername());
        fcmParams.put("token", registerTokenRequestEvent.getToken());

        Call<RegisterTokenResponseEvent> fcmRegisterCall = iUsers.registerToken(fcmParams);
        asyncRequest(fcmRegisterCall);
    }
}
