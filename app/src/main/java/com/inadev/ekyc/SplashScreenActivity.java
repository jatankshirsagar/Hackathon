package com.inadev.ekyc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                /*Intent intent = new Intent(SplashScreenActivity.this, TransactionPopupActivity.class);
                intent.putExtra("title","title");
                intent.putExtra("body","body" +
                        "");
                startActivity(intent);*/
                finish();
            }
        }, 3000);
    }
}
