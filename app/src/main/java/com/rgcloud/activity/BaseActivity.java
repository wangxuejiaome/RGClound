package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.view.StatusBarCompat;
import com.rgcloud.view.TitleBar;



/**
 * Created by Administrator on 2015/12/23.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "BaseActivity";
    protected Context mContext;

    /**
     * 启动activity是否有动画
     */
    protected boolean hasActivityAnim = true;
    private AppActivityManager mActivityManager;
    protected TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivityManager = AppActivityManager.getActivityManager();
        mActivityManager.pushActivity(this);

        //启动activity动画,从右到左
        if (hasActivityAnim)
            overridePendingTransition(R.anim.anim_slide_100to0, R.anim.anim_slide_0tonegative100);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarCompat.compat(this, 0XFF0398FF);
    }

    protected void initTitleBar(int id, String title) {
        initTitleBar(id, title, "", null);
    }

    protected void initTitleBar(int resId, String title, String left, Object right) {

        titleBar = (TitleBar) findViewById(resId);

        if (!TextUtils.isEmpty(title)) {
            titleBar.tvTitle.setVisibility(View.VISIBLE);
            titleBar.tvTitle.setText(title);
        } else {
            titleBar.tvTitle.setVisibility(View.GONE);
        }

        //传null不显示btn
        //left有值，且不是"",显示btn
        if (left == null) {
            titleBar.btnLeft.setVisibility(View.GONE);
        } else {
            titleBar.btnLeft.setText(left);
            titleBar.btnLeft.setVisibility(View.VISIBLE);
            titleBar.btnLeft.setOnClickListener(this);
        }

        //传null不显示btn
        //right有值，是String,显示文字
        //right有值，是Integer,设置图片资源
        if (right == null) {
            titleBar.btnRight.setVisibility(View.GONE);
            titleBar.imgBtnRight.setVisibility(View.GONE);
        } else {
            if (right instanceof String) {
                titleBar.btnRight.setText((CharSequence) right);
                titleBar.imgBtnRight.setVisibility(View.GONE);
                titleBar.btnRight.setVisibility(View.VISIBLE);
            } else {
                titleBar.imgBtnRight.setImageResource((Integer) right);
                titleBar.imgBtnRight.setVisibility(View.VISIBLE);
                titleBar.btnRight.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        finish(true);
    }

    public void finish(boolean hasActivityAnim) {
        overridePendingTransition(R.anim.anim_slide_negative100to0,
                R.anim.anim_slide_0to100);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        //从当前界面返回，从左到右
        overridePendingTransition(R.anim.anim_slide_negative100to0,
                R.anim.anim_slide_0to100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_include_title:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityManager.popActivity(this);
        Log.i(TAG, this.getClass().getSimpleName() + "退出");
    }
}
