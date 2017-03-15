package com.example.dione.noticesapp.utilities;

import com.example.dione.noticesapp.api.NoticesClient;
import com.example.dione.noticesapp.event.ErrorResponseEvent;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Donds on 3/15/2017.
 */

public class ErrorUtils {
    public static ErrorResponseEvent parseError(Response<?> response) {
        Converter<ResponseBody, ErrorResponseEvent> converter =
                NoticesClient.mRestAdapter.responseBodyConverter(ErrorResponseEvent.class, new Annotation[0]);

        ErrorResponseEvent error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponseEvent();
        }
        return error;
    }
}
