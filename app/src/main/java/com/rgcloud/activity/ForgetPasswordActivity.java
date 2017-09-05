package com.rgcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rgcloud.R;
import com.rgcloud.entity.request.ForgetPasswordReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.CountDownUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity {


    @Bind(R.id.et_phone_forget_password)
    EditText etPhone;
    @Bind(R.id.et_verify_code_forget_password)
    EditText etVerifyCode;
    @Bind(R.id.btn_verify_code_forget_password)
    Button btnVerifyCode;
    @Bind(R.id.et_new_password_forget_password)
    EditText etNewPassword;
    @Bind(R.id.btn_update_password)
    Button btnUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_forget_password, "忘记密码");
    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(etVerifyCode.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入验证码");
            return false;
        }
        if (TextUtils.isEmpty(etNewPassword.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入新密码");
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
            verifyCodeReqEntity.TemplateId = 2;
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

    private void forgetPassword() {
        if (!checkValidate()) return;
        ForgetPasswordReqEntity forgetPasswordReqEntity = new ForgetPasswordReqEntity();
        forgetPasswordReqEntity.PhoneNumber = etPhone.getText().toString().trim();
        forgetPasswordReqEntity.ValidCode = etVerifyCode.getText().toString().trim();
        forgetPasswordReqEntity.setNewPassword(etNewPassword.getText().toString().trim());
        RequestApi.forgetPassword(forgetPasswordReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("密码重置成功，请重新登录");
                finish();
            }
        });
    }

    @OnClick({R.id.btn_verify_code_forget_password, R.id.btn_update_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_code_forget_password:
                getVerifyCode();
                break;
            case R.id.btn_update_password:
                forgetPassword();
                break;
        }
    }
}
