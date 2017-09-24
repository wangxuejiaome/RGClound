package com.rgcloud.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.entity.request.BindPhoneReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.CountDownUtil;
import com.rgcloud.util.ToastUtil;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.login.TCRegisterMgr;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;
import com.tencent.rtmp.TXLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity implements TCRegisterMgr.TCRegisterCallback {

    @Bind(R.id.et_phone_bind_phone)
    EditText etPhone;
    @Bind(R.id.et_password_bind_phone)
    EditText etPassword;
    @Bind(R.id.et_verify_code_bind_phone)
    EditText etVerifyCode;
    @Bind(R.id.btn_verify_code_bind_phone)
    Button btnVerifyCode;

    private TCRegisterMgr mTCRegisterMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        mTCRegisterMgr = TCRegisterMgr.getInstance();
        mTCRegisterMgr.setTCRegisterCallback(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_bind_phone, "绑定手机号");
    }

    private boolean checkedValidate() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
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
            verifyCodeReqEntity.TemplateId = 7;
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

    private void binPhone() {

        if (!checkedValidate()) return;
        final BindPhoneReqEntity bindPhoneReqEntity = new BindPhoneReqEntity();
        bindPhoneReqEntity.PhoneNumber = etPhone.getText().toString().trim();
        bindPhoneReqEntity.ValidCode = etVerifyCode.getText().toString().trim();
        bindPhoneReqEntity.setPassword(etPassword.getText().toString().trim());
        RequestApi.bindPhone(bindPhoneReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);

                //在腾讯云上注册
                mTCRegisterMgr.pwdRegist(bindPhoneReqEntity.PhoneNumber, "12345678");
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
                TXLog.d("BindPhone", "login after regist success");
            }

            @Override
            public void onFailure(int code, String msg) {
                tcLoginMgr.removeTCLoginCallback();
                TXLog.d("BindPhone", "login after regist fail, code:" + code + " msg:" + msg);
            }
        });

        //tcLoginMgr.pwdLogin(identifier, etPassword.getText().toString());
        tcLoginMgr.pwdLogin(identifier, "12345678");
        mTCRegisterMgr.removeTCRegisterCallback();

        CirCleLoadingDialogUtil.dismissCircleProgressDialog();
        ToastUtil.showShortToast("绑定成功");
        //PreferencesUtil preferencesUtil = new PreferencesUtil(mContext);
        finish();
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

    @OnClick({R.id.btn_verify_code_bind_phone, R.id.btn_bind_phone})
    public void onViewClicked(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_verify_code_bind_phone:
                getVerifyCode();
                break;
            case R.id.btn_bind_phone:
                binPhone();
                break;
        }
    }


}
