package com.inadev.ekyc.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;


    // Constructor
    FingerprintHandler(Context mContext) {
        context = mContext;
    }


    void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    private void update(String e, Boolean success) {
        if (success) {
            if (onFingerPrintAuthenticationCallback != null) {
                onFingerPrintAuthenticationCallback.onAuthenticationSucceeded();
            }
        } else {
            if (onFingerPrintAuthenticationCallback != null) {
                onFingerPrintAuthenticationCallback.onAuthenticationFailed();
            }
        }
    }

    void setOnFingerPrintAuthenticationCallback(OnFingerPrintAuthenticationCallback onFingerPrintAuthenticationCallback) {
        this.onFingerPrintAuthenticationCallback = onFingerPrintAuthenticationCallback;
    }

    private OnFingerPrintAuthenticationCallback onFingerPrintAuthenticationCallback;

    public interface OnFingerPrintAuthenticationCallback {
        void onAuthenticationFailed();

        void onAuthenticationSucceeded();
    }
}