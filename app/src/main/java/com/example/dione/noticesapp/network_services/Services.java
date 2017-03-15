package com.example.dione.noticesapp.network_services;

import com.example.dione.noticesapp.api.NoticesClient;
import com.example.dione.noticesapp.api.interfaces.IUsers;
import com.example.dione.noticesapp.application.NoticesApplication;
import com.example.dione.noticesapp.event.RegisterRequestEvent;
import com.example.dione.noticesapp.event.RegisterResponseEvent;
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
    public void onSendDealSearchEvent(RegisterRequestEvent registerRequestEvent) {
        IUsers iUsers = NoticesClient.mRestAdapter.create(IUsers.class);
        Map<String, String> dealSearchParams = new HashMap<>();
        dealSearchParams.put("email", registerRequestEvent.getEmail());
        dealSearchParams.put("nickname", registerRequestEvent.getNickname());
        dealSearchParams.put("password", registerRequestEvent.getPassword());

        Call<RegisterResponseEvent> dealSearchModelCall = iUsers.registerUser(dealSearchParams);
        asyncRequest(dealSearchModelCall);
    }
}
