package com.rgcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rgcloud.R;
import com.rgcloud.util.PreferencesUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    PreferencesUtil mPreferencesUtil;
    @Bind(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mPreferencesUtil = new PreferencesUtil(this);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "splashBg.jpg");
        if (file.exists()) {
            //加载图片
            Glide.with(SplashActivity.this).load(file).placeholder(R.mipmap.splash_bg).into(ivSplash);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
                finish();
            }
        }, 2000);
    }
}
