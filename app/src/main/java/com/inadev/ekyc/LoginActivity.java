package com.inadev.ekyc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.inadev.ekyc.api.ApiClient;
import com.inadev.ekyc.api.ApiInterface;
import com.inadev.ekyc.api.request.LoginRequest;
import com.inadev.ekyc.api.response.LoginResponse;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseCode();
        authenticateUser(new LoginRequest());
    }

    private void firebaseCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.google_app_id);
            String channelName = getString(R.string.gcm_defaultSenderId);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        Utils.showLog("subscribed");

        authenticateUser(new LoginRequest());
    }

    private void authenticateUser(LoginRequest loginRequest) {
        showProgress(this);
        ApiInterface apiInterface = ApiClient.getAppServiceClient().create(ApiInterface.class);

        apiInterface.authenticateUser(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                        dissmissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if(loginResponse!=null)
                        {
                            Toast.makeText(LoginActivity.this,loginResponse.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
