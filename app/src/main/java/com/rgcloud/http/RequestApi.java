package com.rgcloud.http;

import com.rgcloud.entity.request.RegisterRequestEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;

import retrofit2.Call;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RequestApi {

    /**
     * 获取验证码
     */
    public static void getVerifyCode(VerifyCodeReqEntity verifyCodeReqEntity, ResponseCallBack responseCallBack){
        Call<VerifyCodeResEntity> call = ServiceGenerator.createService(RGCloudServices.class,true).getVerifyCode(verifyCodeReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 注册
     */
    public static void register(RegisterRequestEntity registerRequestEntity,ResponseCallBack responseCallBack){
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class,true).register(registerRequestEntity);
        call.enqueue(responseCallBack);
    }


}
