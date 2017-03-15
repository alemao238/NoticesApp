package com.example.dione.noticesapp.firebase;

import android.util.Log;

import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.event.RegisterTokenRequestEvent;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Donds on 3/10/2017.
 */

public class InstanceService extends com.google.firebase.iid.FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        sharedPreferenceManager.saveStringPreference(ApplicationConstants.KEY_REG_TOKEN, refreshedToken);
    }
}
