package com.inadev.ekyc.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class LoginRequest
{
    @SerializedName("username")
    private String uname = "rajendra";

    @SerializedName("password")
    private String pass = "asdfg@123";
}
