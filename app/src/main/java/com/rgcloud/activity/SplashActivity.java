package com.rgcloud.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rgcloud.R;
import com.rgcloud.util.PreferencesUtil;

public class SplashActivity extends BaseActivity {

    PreferencesUtil mPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPreferencesUtil = new PreferencesUtil(mContext);
    }


    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(mContext, Main2Activity.class));
                finish();
            }
        }, 2000);
    }
}