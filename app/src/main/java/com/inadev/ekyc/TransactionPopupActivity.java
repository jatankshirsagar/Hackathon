package com.inadev.ekyc;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionPopupActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    AppCompatTextView title;

    @BindView(R.id.tv_message)
    AppCompatTextView message;


    @OnClick(R.id.btn_yes)
    public void OnYesClicked(View view) {

    }

    @OnClick(R.id.btn_no)
    public void OnNoClicked(View view) {

    }

    @OnClick(R.id.iv_alert)
    public void OnAlertClicked(View view) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.authentication_screen);
        ButterKnife.bind(this);
        firebaseCode();
    }

    private void firebaseCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.google_app_id);
            String channelName = getString(R.string.gcm_defaultSenderId);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            title.setText(getIntent().getStringExtra("title"));
            message.setText(getIntent().getStringExtra("body"));
        }

        // Log and toast
        Utils.showLog("subscribed");
    }
}
