package com.neolab.baseretrofit20.models;

/**
 * Copyright © 2016 AsianTech Inc.
 * Created by TanHN on 8/8/16.
 */
public class User extends BaseModel {
    private int id;
    private String access_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
