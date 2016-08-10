package com.neolab.baseretrofit20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.neolab.baseretrofit20.models.User;
import com.neolab.baseretrofit20.retrofit2.Api;
import com.neolab.baseretrofit20.retrofit2.core.ApiClient;
import com.neolab.baseretrofit20.retrofit2.core.MyCallback;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiClient.getClient().create(Api.class).login("kindergarten_test", "123456", "sdsssddssd").enqueue(new MyCallback<User>() {
            @Override
            public void onHHSuccess(Response<User> response) {
                Log.d(TAG, "onHHSuccess" + response.body().getAccess_token());
            }

            @Override
            public void onHHFailure(String message) {
                Log.d(TAG, "onHHFailure" + message);
            }

        });
    }
}
