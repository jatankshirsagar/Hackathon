package com.inadev.ekyc;


import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TransactionPopupActivity extends AppCompatActivity {

    private StringBuilder bodymessage = new StringBuilder();

    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;

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

    }

    @OnClick(R.id.btn_no)
    public void OnNoClicked(View view) {

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
        // Keyguard Manager
        KeyguardManager keyguardManager = (KeyguardManager)
                getSystemService(KEYGUARD_SERVICE);

        // Fingerprint Manager
        fingerprintManager = (FingerprintManager)
                getSystemService(FINGERPRINT_SERVICE);

        if (!checkFinger()) {
            ivFingerPrintImage.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnYes.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.VISIBLE);
        } else {
            bodymessage.append("\n\n");
            bodymessage.append(getString(R.string.scan_fingerprint));
        }
        message.setText(Html.fromHtml(bodymessage.toString()));
        Linkify.addLinks(tv_report, Linkify.ALL);
    }

    private boolean checkFinger() {
        Utils.showLog("check Finger");
        // Keyguard Manager
        KeyguardManager keyguardManager = (KeyguardManager)
                getSystemService(KEYGUARD_SERVICE);

        // Fingerprint Manager
        fingerprintManager = (FingerprintManager)
                getSystemService(FINGERPRINT_SERVICE);

        try {
            // Check if the fingerprint sensor is present
            if (!fingerprintManager.isHardwareDetected()) {
                Utils.showLog("::::> " + message);
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Utils.showLog("::::> " + message);
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                Utils.showLog("::::> " + message);
                return false;
            } else {
                return true;
            }

        } catch (SecurityException se) {
            se.printStackTrace();
            return false;
        }

    }
}
