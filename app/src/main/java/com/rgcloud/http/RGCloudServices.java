package com.rgcloud.http;

import com.rgcloud.entity.BaseResEntity;
import com.rgcloud.entity.request.RegisterRequestEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public interface RGCloudServices {

    /**
     * 获取验证码
     */
    @POST("commonApi/sendMessage")
    Call<VerifyCodeResEntity> getVerifyCode(@Body VerifyCodeReqEntity verifyCodeReqEntity);

    /**
     * 注册
     */
    @POST("register")
    Call<TokenResEntity> register(@Body RegisterRequestEntity registerRequestEntity);
}
