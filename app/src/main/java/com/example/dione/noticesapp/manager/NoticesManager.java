package com.example.dione.noticesapp.manager;

import android.content.Context;

import com.example.dione.noticesapp.api.NoticesClient;
import com.squareup.otto.Bus;


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

}
