package com.rgcloud.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.config.Constant;
import com.rgcloud.entity.request.LoginReqEntity;
import com.rgcloud.entity.request.WXReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.WXOpenIdResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.PreferencesUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.xiaozhibo.TCApplication;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.userinfo.ITCUserInfoMgrListener;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rgcloud.wxapi.WXEntryActivity.code;

public class LoginActivity extends BaseActivity implements ResponseCallBack.LoginInterface, TCLoginMgr.TCLoginCallback {

    @Bind(R.id.tb_login)
    TitleBar tbLogin;
    @Bind(R.id.et_phone_login)
    EditText etPhone;
    @Bind(R.id.et_password_login)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_wx_login)
    ImageButton btnWxLogin;

    /**
     * 登录类型：0.普通登录；1.强制登录；2.token异常
     */
    private int mLoginType;
    private static PreferencesUtil mPreferencesUtil;

    private TCLoginMgr mTCLoginMgr;
    private TokenResEntity mTokenResEntity;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        mPreferencesUtil = new PreferencesUtil(mContext);
        ResponseCallBack.setLoginInterface(this);
        mTCLoginMgr = TCLoginMgr.getInstance();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, false);
        api.registerApp(Constant.WX_APP_ID);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(R.id.tb_login, "登录", "", "注册");
    }

    private void initData() {
        mLoginType = getIntent().getIntExtra("loginType", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置登录回调,resume设置回调避免被registerActivity冲掉
        mTCLoginMgr.setTCLoginCallback(this);
    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入密码");
            return false;
        }
        return true;
    }

    private void login() {
        if (!checkValidate()) return;
        LoginReqEntity loginReqEntity = new LoginReqEntity();
        loginReqEntity.LoginPhone = etPhone.getText().toString().trim();
        loginReqEntity.setPassword(etPassword.getText().toString().trim());
        loginReqEntity.EquipmentId = JPushInterface.getRegistrationID(mContext);
        loginReqEntity.LoginType = mLoginType;
        RequestApi.login(loginReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                mTokenResEntity = (TokenResEntity) resEntity;

                mTCLoginMgr.setTCLoginCallback(LoginActivity.this);
                //调用LoginHelper进行普通登录
                // mTCLoginMgr.pwdLogin(mTokenResEntity.MemberNickName, etPassword.getText().toString().trim());
                mTCLoginMgr.pwdLogin(mTokenResEntity.MemberNickName, "12345678");
            }
        });
    }

    public static void getWXOpenId() {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.WX_APP_ID + "&secret=" + Constant.WX_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
        RequestApi.getWXOpenId(url, new Callback<WXOpenIdResEntity>() {
            @Override
            public void onResponse(Call<WXOpenIdResEntity> call, Response<WXOpenIdResEntity> response) {
                WXOpenIdResEntity wxOpenIdResEntity = response.body();
                wxLogin(wxOpenIdResEntity);
            }

            @Override
            public void onFailure(Call<WXOpenIdResEntity> call, Throwable t) {
                ToastUtil.showShortToast("微信登录失败");
            }
        });
    }

    private static void wxLogin(WXOpenIdResEntity wxOpenIdResEntity) {
        WXReqEntity wxReqEntity = new WXReqEntity();
        wxReqEntity.OpenId = wxOpenIdResEntity.openid;
        wxReqEntity.UnionId = wxOpenIdResEntity.unionid;
        wxReqEntity.EquipmentId = JPushInterface.getRegistrationID(TCApplication.getApplication());
        wxReqEntity.LoginType = 0;
        RequestApi.wxLogin(wxReqEntity, new ResponseCallBack(TCApplication.getApplication()) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                TokenResEntity tokenResEntity = (TokenResEntity) resEntity;
                mPreferencesUtil.put(PreferencesUtil.ACCESS_TOKEN, tokenResEntity.Token);
                mPreferencesUtil.put(PreferencesUtil.HAS_LOGIN, true);
                mPreferencesUtil.put(PreferencesUtil.ISWXLOGIN, true);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("登录成功");
                AppActivityManager.getActivityManager().getCurrentActivity().startActivity(new Intent(TCApplication.getApplication(), Main2Activity.class));
            }
        });
    }


    @Override
    public void loginAnyway() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder
                .setMessage("您的账户已在别处登陆，是否强制登陆")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoginType = 1;
                        login();
                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onSuccess() {

        TCUserInfoMgr.getInstance().setUserId(mTCLoginMgr.getLastUserInfo().identifier, new ITCUserInfoMgrListener() {
            @Override
            public void OnQueryUserInfo(int error, String errorMsg) {
            }

            @Override
            public void OnSetUserInfo(int error, String errorMsg) {
                if (0 != error)
                    Toast.makeText(getApplicationContext(), "设置 User ID 失败" + errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        mTCLoginMgr.removeTCLoginCallback();
        mPreferencesUtil.put(PreferencesUtil.ACCESS_TOKEN, mTokenResEntity.Token);
        mPreferencesUtil.put(PreferencesUtil.HAS_LOGIN, true);
        CirCleLoadingDialogUtil.dismissCircleProgressDialog();
        ToastUtil.showShortToast("登录成功");
        startActivity(new Intent(mContext, Main2Activity.class));
    }

    @Override
    public void onFailure(int code, String msg) {

        //被踢下线后弹窗显示被踢
        if (6208 == code) {
            TCUtils.showKickOutDialog(this);
        }
        Toast.makeText(getApplicationContext(), "登录失败" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //删除登录回调
        mTCLoginMgr.removeTCLoginCallback();
    }

    @OnClick({R.id.tv_forget_password, R.id.btn_login, R.id.btn_wx_login, R.id.btn_right_include_title})
    public void onViewClicked(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_right_include_title:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                mPreferencesUtil.put(PreferencesUtil.HAS_LOGIN, false);
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_wx_login:
                if (!api.isWXAppInstalled()) {
                    ToastUtil.showShortToast("您还未安装微信客户端");
                    return;
                }
                // send oauth request
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_android";
                api.sendReq(req);
                break;

        }
    }


}
