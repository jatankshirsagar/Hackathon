package com.inadev.ekyc.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class LoginRequest
{
    @SerializedName("Username")
    private String uname;

    @SerializedName("Password")
    private String pass;
}
