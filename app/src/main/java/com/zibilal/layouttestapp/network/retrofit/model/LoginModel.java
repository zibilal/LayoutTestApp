package com.zibilal.layouttestapp.network.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bilal on 1/15/2016.
 */
public class LoginModel {
    @SerializedName("grant_type")
    private String grantType;
    private String username;
    private String password;

    public LoginModel() {}

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
