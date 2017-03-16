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
import com.example.dione.noticesapp.utilities.ApplicationConstants;
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
        registerParams.put(ApplicationConstants.KEY_EMAIL, registerRequestEvent.getEmail());
        registerParams.put(ApplicationConstants.KEY_NAME, registerRequestEvent.getNickname());
        registerParams.put(ApplicationConstants.KEY_PASSWORD, registerRequestEvent.getPassword());
        registerParams.put(ApplicationConstants.KEY_USERNAME, registerRequestEvent.getUsername());
        registerParams.put(ApplicationConstants.KEY_TYPE, ApplicationConstants.KEY_USER_TYPE);
        registerParams.put(ApplicationConstants.KEY_PHOTO, registerRequestEvent.getPhotoUrl());
        Call<RegisterResponseEvent> registerCall = iUsers.registerUser(registerParams);
        asyncRequest(registerCall);
    }

    @Subscribe
    public void onSendLoginEvent(LoginRequestEvent loginRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put(ApplicationConstants.KEY_TYPE, loginRequestEvent.getType());
        loginParams.put(ApplicationConstants.KEY_USERNAME, loginRequestEvent.getUsername());
        loginParams.put(ApplicationConstants.KEY_PASSWORD, loginRequestEvent.getPassword());

        Call<LoginResponseEvent> loginCall = iUsers.loginUser(loginParams);
        asyncRequest(loginCall);
    }

    @Subscribe
    public void onSendFcmToken(RegisterTokenRequestEvent registerTokenRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> fcmParams = new HashMap<>();
        fcmParams.put(ApplicationConstants.KEY_TYPE, registerTokenRequestEvent.getType());
        fcmParams.put(ApplicationConstants.KEY_USERNAME, registerTokenRequestEvent.getUsername());
        fcmParams.put(ApplicationConstants.KEY_UID, registerTokenRequestEvent.getUid());
        fcmParams.put(ApplicationConstants.KEY_TOKEN, registerTokenRequestEvent.getToken());

        Call<RegisterTokenResponseEvent> fcmRegisterCall = iUsers.registerToken(fcmParams);
        asyncRequest(fcmRegisterCall);
    }
}
