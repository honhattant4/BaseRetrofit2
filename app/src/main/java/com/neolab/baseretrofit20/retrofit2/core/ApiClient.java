package com.neolab.baseretrofit20.retrofit2.core;

import android.util.Base64;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neolab.baseretrofit20.BuildConfig;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Copyright © 2016 AsianTech Inc.
 * Created by TanHN on 8/8/16.
 */
public class ApiClient {

    private static final int TIMEOUT_CONNECTION = 30000;
    private static final String HEADER_KEY = "Authenticate-Key";
    private static final String HEADER_SIGNATURE = "Authenticate-Signature";
    private static final String TAG = ApiClient.class.getSimpleName();
    private static Retrofit retrofit = null;


    public static synchronized Retrofit getClient() {
        if (retrofit == null) {
         /* Create Gson */
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                    .setExclusionStrategies(new ExclusionStrategy() {
//                        @Override
//                        public boolean shouldSkipField(FieldAttributes f) {
//                            return f.getDeclaringClass().equals(RealmObject.class);
//                        }
//
//                        @Override
//                        public boolean shouldSkipClass(Class<?> clazz) {
//                            return false;
//                        }
//                    })
//                    .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
//                    }.getType(), new RealmStringDeserializer())
                    .create();


          /* Config OkHttpClient */
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            okHttpClient.addInterceptor(logging);
            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws java.io.IOException {
                    Request original = chain.request();
                    Log.d("url=", original.method());
                    Log.d("url=", original.url().toString());

                    // Request customization: add request headers
                    String signature = "";
                    try {
                        signature = getSignature(original.method() + ":" + original.url().toString());
                    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, signature);
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader(HEADER_SIGNATURE, signature)
                            .addHeader(HEADER_KEY, BuildConfig.API_KEY); // <-- this is the important line

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            okHttpClient.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://54.197.125.234/api/v1/")
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private static String getSignature(String method) throws NoSuchAlgorithmException, InvalidKeyException {
        String secret = BuildConfig.API_SECRECT;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
        sha256_HMAC.init(secret_key);
        return Base64.encodeToString(sha256_HMAC.doFinal(method.getBytes()), Base64.NO_WRAP);
    }

}