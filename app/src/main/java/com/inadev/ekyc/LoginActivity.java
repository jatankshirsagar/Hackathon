package com.inadev.ekyc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.inadev.ekyc.api.ApiClient;
import com.inadev.ekyc.api.ApiInterface;
import com.inadev.ekyc.api.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by OPTLPTP163 on 9/16/2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_emailid)
    EditText loginEmailid;

    @BindView(R.id.login_password)
    EditText loginPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.loginBtn)
     void authenticateUser() {
        if(validation())
        {
            Map<String,String> loginRequest = new HashMap<>();
            loginRequest.put("username",loginEmailid.getText().toString());
            loginRequest.put("password",loginPassword.getText().toString());
            loginRequest.put("token", FirebaseInstanceId.getInstance().getToken());


            showProgress(this);
            ApiInterface apiInterface = ApiClient.getAppServiceClient().create(ApiInterface.class);

            apiInterface.authenticateUser(loginRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseResponse>() {
                        @Override
                        public void onCompleted() {
                            dissmissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dissmissProgressDialog();
                            Toast.makeText(LoginActivity.this,""+e,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(BaseResponse loginResponse) {
                            if(loginResponse!=null)
                            {
                                Toast.makeText(LoginActivity.this,loginResponse.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private boolean validation() {
        if(loginEmailid.getText().toString().isEmpty() && loginPassword.getText().toString().isEmpty()) {
            Toast.makeText(this,"Please enter username and password..",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(loginEmailid.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Please enter username..",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(loginPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Please enter password..",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
}
