package com.example.dione.noticesapp.application;

import android.app.Application;

import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.manager.NoticesManager;
import com.example.dione.noticesapp.network_services.Services;
import com.squareup.otto.Bus;

/**
 * Created by dione on 11/08/2016.
 */
public class NoticesApplication extends Application {
    private NoticesManager mNoticesManager;
    public Bus mBus = BusProvider.getInstance();
    @Override
    public void onCreate() {
        super.onCreate();
        mNoticesManager = new NoticesManager(this, mBus);
        new Services(this);
        mBus.register(mNoticesManager);
        mBus.register(this);
    }
}
