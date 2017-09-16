package com.inadev.ekyc.api;

import com.inadev.ekyc.api.request.LoginRequest;
import com.inadev.ekyc.api.response.LoginResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public interface ApiInterface {

    @POST(APIConstants.PATH_LOGIN)
    @Headers({APIConstants.HEADER_CONTENT_TYPE})
    Observable<LoginResponse> authenticateUser(@Body LoginRequest loginRequest);
}
