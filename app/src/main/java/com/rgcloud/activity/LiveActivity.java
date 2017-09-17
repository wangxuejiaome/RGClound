package com.rgcloud.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.rgcloud.R;
import com.rgcloud.fragment.LiveListFragment;
import com.tencent.TIMManager;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.mainui.list.TCLiveListFragment;
import com.tencent.qcloud.xiaozhibo.push.TCPublishSettingActivity;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;

import tencent.tls.platform.TLSUserInfo;

/**
 * 主界面，包括直播列表，用户信息页
 * UI使用FragmentTabHost+Fragment
 * 直播列表：TCLiveListFragment
 * 个人信息页：TCUserInfoFragment
 */
public class LiveActivity extends FragmentActivity {
    private static final String TAG = LiveActivity.class.getSimpleName();

    //被踢下线广播监听
    private LocalBroadcastManager mLocalBroadcatManager;
    private BroadcastReceiver mExitBroadcastReceiver;

    private long mLastClickPubTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        LiveListFragment liveListFragment = new LiveListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contentPanel, liveListFragment);
        fragmentTransaction.commit();

        LinearLayout llLive = (LinearLayout) findViewById(R.id.ll_start_play);
        ImageView ivStartPlay = (ImageView) findViewById(R.id.iv_start_live);

        if (getIntent().getBooleanExtra("isHost", false)) {//是主播
            llLive.setVisibility(View.VISIBLE);
            ivStartPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (System.currentTimeMillis() - mLastClickPubTS > 1000) {
                        mLastClickPubTS = System.currentTimeMillis();
                        startActivity(new Intent(LiveActivity.this, TCPublishSettingActivity.class));
                    }
                }
            });
        } else {
            llLive.setVisibility(View.GONE);
        }

        mLocalBroadcatManager = LocalBroadcastManager.getInstance(this);
        mExitBroadcastReceiver = new ExitBroadcastRecevier();
        mLocalBroadcatManager.registerReceiver(mExitBroadcastReceiver, new IntentFilter(TCConstants.EXIT_APP));

        Log.w("TCLog", "mainactivity oncreate");


        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("TCLog", "mainactivity onstart");
        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
            //relogin
            final TCLoginMgr tcLoginMgr = TCLoginMgr.getInstance();
            final TLSUserInfo userInfo = TCLoginMgr.getInstance().getLastUserInfo();
            tcLoginMgr.setTCLoginCallback(new TCLoginMgr.TCLoginCallback() {
                @Override
                public void onSuccess() {
                    tcLoginMgr.removeTCLoginCallback();
                    TCUserInfoMgr.getInstance().setUserId(userInfo.identifier, null);
                }

                @Override
                public void onFailure(int code, String msg) {
                    tcLoginMgr.removeTCLoginCallback();
                }
            });

            tcLoginMgr.checkCacheAndLogin();
            Log.w("TCLog", "mainactivity onstart relogin");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcatManager.unregisterReceiver(mExitBroadcastReceiver);
    }

    public class ExitBroadcastRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TCConstants.EXIT_APP)) {
                onReceiveExitMsg();
            }
        }
    }

    public void onReceiveExitMsg() {
        TCUtils.showKickOutDialog(this);
    }

}
