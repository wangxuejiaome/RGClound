package com.rgcloud.activity;

import android.os.Bundle;

import com.rgcloud.R;
import com.rgcloud.entity.request.RegisterRequestEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;


public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        RequestApi.register(new RegisterRequestEntity(),new ResponseCallBack(mContext));
    }
}
