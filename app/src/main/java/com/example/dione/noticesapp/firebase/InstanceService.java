package com.example.dione.noticesapp.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Donds on 3/10/2017.
 */

public class InstanceService extends com.google.firebase.iid.FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("REFRESHED_TOKEN", refreshedToken);
    }
}
