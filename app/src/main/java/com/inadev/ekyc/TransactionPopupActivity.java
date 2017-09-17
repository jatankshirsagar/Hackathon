package com.inadev.ekyc;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.inadev.ekyc.api.ApiClient;
import com.inadev.ekyc.api.ApiInterface;
import com.inadev.ekyc.api.response.BaseResponse;
import com.inadev.ekyc.fingerprint.FingerPrintHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("unused")
@RequiresApi(api = Build.VERSION_CODES.M)
public class TransactionPopupActivity extends BaseActivity implements FingerPrintHelper.OnFingerPrintListener {

    private StringBuilder bodymessage = new StringBuilder();

    @BindView(R.id.iv_finger_print)
    AppCompatImageView ivFingerPrintImage;

    @BindView(R.id.btn_report)
    AppCompatTextView tv_report;

    @BindView(R.id.btnCancel)
    AppCompatButton btnCancel;

    @BindView(R.id.tv_title)
    AppCompatTextView title;

    @BindView(R.id.tv_message)
    AppCompatTextView message;

    @BindView(R.id.btn_yes)
    AppCompatButton btnYes;

    @BindView(R.id.btn_no)
    AppCompatButton btnNo;


    @OnClick(R.id.btn_yes)
    public void OnYesClicked(View view) {
        updateTransactionStatus("yes");
    }

    @OnClick(R.id.btn_no)
    public void OnNoClicked(View view) {
        updateTransactionStatus("no");
    }

    @OnClick(R.id.btnCancel)
    public void OnCancelClicked(View view) {
        updateTransactionStatus("no");
    }

    @OnClick(R.id.btn_report)
    public void OnAlertClicked(View view) {
        updateTransactionStatus("report");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.authentication_screen);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            title.setText(getIntent().getStringExtra("title"));
            bodymessage.append(getIntent().getStringExtra("body"));
        }

        if (!FingerPrintHelper.getInstance().checkFinger(this)) {
            ivFingerPrintImage.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnYes.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.VISIBLE);
        } else {
            FingerPrintHelper.getInstance().initFingerprintAuthentication(this);
            bodymessage.append("<div>&nbsp;</div>");
            bodymessage.append(getString(R.string.scan_fingerprint));
        }
        FingerPrintHelper.getInstance().setOnFingerPrintListener(this);
        message.setText(Html.fromHtml(bodymessage.toString()));
        Linkify.addLinks(tv_report, Linkify.ALL);
    }

    @Override
    public void onFingerPrintAuthenticated() {
        Toast.makeText(this, "Authenticated Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFingerPrintNotAuthenticated() {
        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
    }

    private void updateTransactionStatus(String response) {
        Map<String, String> transactionRequest = new HashMap<>();
        transactionRequest.put("response", response);


        showProgress(this);
        ApiInterface apiInterface = ApiClient.getAppServiceClient().create(ApiInterface.class);

        apiInterface.updateTransactionStatus(transactionRequest)
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
                        Toast.makeText(TransactionPopupActivity.this, "" + e, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(BaseResponse loginResponse) {
                        if (loginResponse != null) {
                            Toast.makeText(TransactionPopupActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
