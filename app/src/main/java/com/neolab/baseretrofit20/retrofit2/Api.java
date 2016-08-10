package com.neolab.baseretrofit20.retrofit2;

import com.neolab.baseretrofit20.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by TanHN on 6/2/2016.
 */
public interface Api {

    @FormUrlEncoded
    @POST("kindergarten/login")
    Call<User> login(@Field(Parameter.USER_NAME) String username, @Field(Parameter.PASSWORD) String password, @Field(Parameter.DEVICE_TOKEN) String device_token);
}
