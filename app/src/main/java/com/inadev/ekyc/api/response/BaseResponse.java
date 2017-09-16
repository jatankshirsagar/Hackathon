package com.inadev.ekyc.api.response;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class BaseResponse
{
    private String status;

    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
