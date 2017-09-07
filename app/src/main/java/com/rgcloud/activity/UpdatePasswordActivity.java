package com.rgcloud.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgcloud.R;
import com.rgcloud.entity.request.UpdatePasswordReqEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePasswordActivity extends BaseActivity {

    @Bind(R.id.et_old_password_update_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password_update_password)
    EditText etNewPassword;
    @Bind(R.id.et_new_password_again_update_password)
    EditText etNewPasswordAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_update_password, "修改密码");
    }

    private boolean checkedValidate() {
        if (TextUtils.isEmpty(etOldPassword.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(etNewPassword.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入新密码");
            return false;
        }
        if (TextUtils.isEmpty(etNewPasswordAgain.getText().toString().trim())) {
            ToastUtil.showShortToast("请输再次入原密码");
            return false;
        } else {
            if (!etNewPasswordAgain.getText().toString().equals(etNewPassword.getText().toString())) {
                ToastUtil.showShortToast("两次输入的密码不一致");
                return false;
            }
        }
        return true;
    }

    private void updatePassword() {
        if (!checkedValidate()) return;
        UpdatePasswordReqEntity updatePasswordReqEntity = new UpdatePasswordReqEntity();
        updatePasswordReqEntity.setOldPassword(etOldPassword.getText().toString().trim());
        updatePasswordReqEntity.setNewPassword(etNewPassword.getText().toString().trim());
        RequestApi.updatePassword(updatePasswordReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                ToastUtil.showShortToast("修改密码成功");
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                finish();
            }
        });
    }

    @OnClick({R.id.btn_update_password})
    public void onViewClicked(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_update_password:
                updatePassword();
                break;
        }
    }
}
