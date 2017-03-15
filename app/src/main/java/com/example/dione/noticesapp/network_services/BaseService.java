package com.example.dione.noticesapp.network_services;

import android.util.Log;

import com.example.dione.noticesapp.application.NoticesApplication;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.event.ErrorRequestEvent;
import com.example.dione.noticesapp.event.ErrorResponseEvent;
import com.example.dione.noticesapp.utilities.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Donds on 3/15/2017.
 */
public class BaseService {
    public BaseService(NoticesApplication noticesApplication) {
        BusProvider.getInstance().register(this);
    }

    protected <T> void asyncRequest(Call<T> apiCall) {
        apiCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    BusProvider.getInstance().post(response.body());
                } else {
                    ErrorResponseEvent apiError = ErrorUtils.parseError(response);
                    String errorMessage = "";
                    if (apiError.getErrorMessage() != null) {
                        errorMessage += apiError.getErrorMessage();
                    }
                    BusProvider.getInstance().post(new ErrorRequestEvent(errorMessage));

                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                BusProvider.getInstance().post(new ErrorRequestEvent(t.getMessage()));
            }
        });

    }
}
