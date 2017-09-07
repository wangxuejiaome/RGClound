package com.rgcloud.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.request.LoginReqEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.PreferencesUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.util.Util;
import com.rgcloud.view.CircleLoadingProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_update_password)
    TextView tvPasswordSetting;
    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.btn_login_out)
    Button btnLoginOut;
    private PreferencesUtil mPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPreferencesUtil = new PreferencesUtil(mContext);
        ButterKnife.bind(this);
        setCacheSize();
    }

    private void setCacheSize() {
        try {
            long cacheSize = Util.getFolderSize(mContext.getCacheDir());
            tvCache.setText(Util.getFormatSize(cacheSize));
        } catch (Exception e) {
            tvCache.setText("0M");
            e.printStackTrace();
        }
    }

    private void clearCache() {
        Util.clearFileDirect(mContext.getCacheDir());
        ToastUtil.showShortToast("缓存清除成功");
        tvCache.setText("0M");
    }

    private void loginOut(){
        RequestApi.loginOut(new BaseReqEntity(),new ResponseCallBack(mContext){
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                ToastUtil.showShortToast("成功退出登录");
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                mPreferencesUtil.loginOutRemove();
                startActivity(new Intent(mContext, LoginActivity.class));
                AppActivityManager.getActivityManager().popAllActivityExceptOne(LoginActivity.class);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_update_password, R.id.ll_clear_cache, R.id.btn_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_update_password:
                startActivity(new Intent(mContext,UpdatePasswordActivity.class));
                break;
            case R.id.ll_clear_cache:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1
                        .setMessage("您确定要清除缓存吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearCache();
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
        }
    }
}
