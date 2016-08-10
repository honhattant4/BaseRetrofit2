package com.neolab.baseretrofit20.retrofit2.core;

import android.util.Log;

import com.neolab.baseretrofit20.retrofit2.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * Copyright © 2016 AsianTech Inc.
 * Created by TanHN on 8/8/16.
 */
public abstract class MyCallback<T> implements Callback<T> {
    public abstract void onHHSuccess(Response<T> response);

    public abstract void onHHFailure(String message);

    private final String TAG = MyCallback.class.getSimpleName();

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "onFailure" + t.getMessage());
        onHHFailure(t.getMessage());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onHHSuccess(response);
        } else {
            Converter<ResponseBody, APIError> converter =
                    ApiClient.getClient()
                            .responseBodyConverter(APIError.class, new Annotation[0]);

            APIError error;

            try {
                error = converter.convert(response.errorBody());
                onHHFailure(error.message());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
