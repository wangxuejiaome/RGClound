package com.rgcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.entity.request.RegisterReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.CountDownUtil;
import com.rgcloud.util.PreferencesUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.login.TCRegisterMgr;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;
import com.tencent.rtmp.TXLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static com.rgcloud.wxapi.WXEntryActivity.code;


public class RegisterActivity extends BaseActivity implements TCRegisterMgr.TCRegisterCallback {

    @Bind(R.id.tb_register)
    TitleBar tbRegister;
    @Bind(R.id.et_phone_register)
    EditText etPhone;
    @Bind(R.id.et_nick_name_register)
    EditText etNickName;
    @Bind(R.id.et_password_register)
    EditText etPassword;
    @Bind(R.id.et_verify_code_register)
    EditText etVerifyCode;
    @Bind(R.id.btn_verify_code_register)
    Button btnVerifyCode;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private TCRegisterMgr mTCRegisterMgr;
    private TokenResEntity mTokenResEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);
        mTCRegisterMgr = TCRegisterMgr.getInstance();
        mTCRegisterMgr.setTCRegisterCallback(this);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(R.id.tb_register, "注册");
    }

    private void initData() {

    }

    private boolean checkedValidate() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入昵称");
            return false;
        } else if (etNickName.getText().toString().trim().length() < 4) {
            ToastUtil.showShortToast("昵称不能小于4位");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入密码");
            return false;
        } else if (etPassword.getText().toString().trim().length() < 8) {
            ToastUtil.showShortToast("长度不能小于8位");
        }
        if (TextUtils.isEmpty(etVerifyCode.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入验证码");
            return false;
        }
        return true;
    }


    private void getVerifyCode() {
        final VerifyCodeReqEntity verifyCodeReqEntity = new VerifyCodeReqEntity();
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
            return;
        } else {
            verifyCodeReqEntity.PhoneNumber = etPhone.getText().toString().trim();
            verifyCodeReqEntity.TemplateId = 1;
        }
        RequestApi.getVerifyCode(verifyCodeReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                VerifyCodeResEntity verifyCodeResEntity = (VerifyCodeResEntity) resEntity;
                ToastUtil.showShortToast(verifyCodeResEntity.Message);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                new CountDownUtil(btnVerifyCode, "%s秒", 120).start();
            }
        });
    }

    private void register() {
        if (!checkedValidate()) return;
        final RegisterReqEntity registerReqEntity = new RegisterReqEntity();
        registerReqEntity.LoginPhone = etPhone.getText().toString().trim();
        registerReqEntity.NickName = etNickName.getText().toString().trim();
        registerReqEntity.setPassword(etPassword.getText().toString().trim());
        registerReqEntity.ValidCode = etVerifyCode.getText().toString().trim();
        registerReqEntity.EquipmentId = JPushInterface.getRegistrationID(mContext);
        RequestApi.register(registerReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mTokenResEntity = (TokenResEntity) resEntity;
                //在腾讯云上注册
                // mTCRegisterMgr.pwdRegist(registerReqEntity.NickName, etPassword.getText().toString());
                mTCRegisterMgr.pwdRegist(registerReqEntity.NickName, "12345678");
            }
        });
    }

    @Override
    public void onSuccess(final String identifier) {

        //自动登录逻辑
        final TCLoginMgr tcLoginMgr = TCLoginMgr.getInstance();
        tcLoginMgr.setTCLoginCallback(new TCLoginMgr.TCLoginCallback() {
            @Override
            public void onSuccess() {
                tcLoginMgr.removeTCLoginCallback();
                TCUserInfoMgr.getInstance().setUserId(identifier, null);
                TXLog.d("TCRegister", "login after regist success");
            }

            @Override
            public void onFailure(int code, String msg) {
                tcLoginMgr.removeTCLoginCallback();
                TXLog.d("TCRegister", "login after regist fail, code:" + code + " msg:" + msg);
            }
        });


        TXLog.d("TCRegister", "注册的用户名是:" + identifier);
        //tcLoginMgr.pwdLogin(identifier, etPassword.getText().toString());
        tcLoginMgr.pwdLogin(identifier, "12345678");
        mTCRegisterMgr.removeTCRegisterCallback();

        CirCleLoadingDialogUtil.dismissCircleProgressDialog();
        ToastUtil.showShortToast("注册成功");
        PreferencesUtil preferencesUtil = new PreferencesUtil(mContext);
        preferencesUtil.put(PreferencesUtil.ACCESS_TOKEN, mTokenResEntity.Token);
        preferencesUtil.put(PreferencesUtil.HAS_LOGIN, true);
        preferencesUtil.put(PreferencesUtil.USER_PHONE, etPhone.getText().toString().trim());
        startActivity(new Intent(mContext, Main2Activity.class));
    }

    @Override
    public void onFailure(int code, String msg) {
        CirCleLoadingDialogUtil.dismissCircleProgressDialog();
        Log.d("TCRegister", "regist fail, code:" + code + " msg:" + msg);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTCRegisterMgr.removeTCRegisterCallback();
    }

    @OnClick({R.id.btn_verify_code_register, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_code_register:
                getVerifyCode();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }


}
