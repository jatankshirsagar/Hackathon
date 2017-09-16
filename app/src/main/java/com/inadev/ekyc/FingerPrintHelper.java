package com.inadev.ekyc;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class FingerPrintHelper implements FingerprintHandler.OnFingerPrintAuthenticationCallback {

    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;

    private static FingerPrintHelper instance;

    private FingerPrintHelper() {

    }

    public static FingerPrintHelper getInstance() {
        if (instance == null) {
            instance = new FingerPrintHelper();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkFinger(Context context) {
        Utils.showLog("check Finger");
        // Keyguard Manager
        keyguardManager = (KeyguardManager)
                context.getSystemService(KEYGUARD_SERVICE);

        // Fingerprint Manager
        fingerprintManager = (FingerprintManager)
                context.getSystemService(FINGERPRINT_SERVICE);

        try {
            // Check if the fingerprint sensor is present
            if (!fingerprintManager.isHardwareDetected()) {
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                return false;
            } else {
                return true;
            }

        } catch (SecurityException se) {
            se.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initFingerprintAuthentication(Context context) {
        // Check whether at least one fingerprint is registered
        // Checks whether lock screen security is enabled or not
        generateKey();
        if (cipherInit()) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            FingerprintHandler helper = new FingerprintHandler(context);
            helper.setOnFingerPrintAuthenticationCallback(this);
            helper.startAuth(fingerprintManager, cryptoObject);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | IOException e) {
            throw new RuntimeException(e);
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            try {
                keyStore.load(null);
            } catch (java.security.cert.CertificateException e) {
                e.printStackTrace();
            }
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void setOnFingerPrintListener(OnFingerPrintListener onFingerPrintListener) {
        this.onFingerPrintListener = onFingerPrintListener;
    }

    OnFingerPrintListener onFingerPrintListener;

    public interface OnFingerPrintListener {
        void onFingerPrintAuthenticated();

        void onFingerPrintNotAuthenticated();
    }

    @Override
    public void onAuthenticationFailed() {
        if (onFingerPrintListener != null) {
            onFingerPrintListener.onFingerPrintNotAuthenticated();
        }
    }

    @Override
    public void onAuthenticationSucceeded() {
        if (onFingerPrintListener != null) {
            onFingerPrintListener.onFingerPrintAuthenticated();
        }
    }
}
