package com.inadev.ekyc.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class LoginRequest
{
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @SerializedName("username")

    private String uname;

    @SerializedName("password")
    private String pass;

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
