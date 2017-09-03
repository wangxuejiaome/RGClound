package com.rgcloud.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rgcloud.R;
import com.rgcloud.entity.request.RegisterRequestEntity;
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

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


public class RegisterActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(R.id.tb_register,"注册");
    }

    private void initData() {

    }

    private boolean checkedValidate(){
        if(TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }
        if(TextUtils.isEmpty(etNickName.getText().toString().trim())){
            ToastUtil.showShortToast("请输入昵称");
            return false;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString().trim())){
            ToastUtil.showShortToast("请输入密码");
            return false;
        }
        if(TextUtils.isEmpty(etVerifyCode.getText().toString().trim())){
            ToastUtil.showShortToast("请输入验证码");
            return false;
        }
        return true;
    }

    private void getVerifyCode(){
        final VerifyCodeReqEntity verifyCodeReqEntity = new VerifyCodeReqEntity();
        if(TextUtils.isEmpty(etPhone.getText().toString().trim())){
            ToastUtil.showShortToast("请输入手机号");
            return;
        }else {
            verifyCodeReqEntity.PhoneNumber = etPhone.getText().toString().trim();
            verifyCodeReqEntity.TemplateId = 1;
        }
        RequestApi.getVerifyCode(verifyCodeReqEntity,new ResponseCallBack(mContext){
            @Override
            public void onObjectResponse(Object resEntity) {
                VerifyCodeResEntity verifyCodeResEntity = (VerifyCodeResEntity) resEntity;
                ToastUtil.showShortToast(verifyCodeResEntity.Message);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                new CountDownUtil(btnVerifyCode, "%s秒", 120).start();
            }
        });
    }

    private void register(){
        if(!checkedValidate()) return;
        RegisterRequestEntity registerRequestEntity = new RegisterRequestEntity();
        registerRequestEntity.LoginPhone = etPhone.getText().toString().trim();
        //registerRequestEntity.
        registerRequestEntity.setPassword(etPassword.getText().toString().trim());
        registerRequestEntity.ValidCode = etVerifyCode.getText().toString().trim();
        registerRequestEntity.EquipmentId = JPushInterface.getRegistrationID(mContext);
        RequestApi.register(registerRequestEntity,new ResponseCallBack(mContext){
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if(resEntity == null) return;
                TokenResEntity tokenResEntity = (TokenResEntity) resEntity;
                PreferencesUtil preferencesUtil = new PreferencesUtil(mContext);
                preferencesUtil.put(PreferencesUtil.ACCESS_TOKEN,tokenResEntity.Token);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
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
