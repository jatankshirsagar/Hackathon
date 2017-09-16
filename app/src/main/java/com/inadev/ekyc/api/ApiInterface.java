package com.inadev.ekyc.api;

import com.inadev.ekyc.api.response.LoginResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(APIConstants.PATH_LOGIN)
    Observable<LoginResponse> authenticateUser(@FieldMap Map<String,String> map);
}
