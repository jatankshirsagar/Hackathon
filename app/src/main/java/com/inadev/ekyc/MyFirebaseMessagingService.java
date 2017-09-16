package com.inadev.ekyc;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Utils.showLog("Data: " + title);
        Utils.showLog("Data: " + body);
        Intent intent = new Intent(new Intent(this, TransactionPopupActivity.class));
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        startActivity(intent);
    }
}