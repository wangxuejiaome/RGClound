package com.rgcloud.http;

import com.rgcloud.entity.request.LoginReqEntity;
import com.rgcloud.entity.request.RegisterReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    @POST("memberApi/register")
    Call<TokenResEntity> register(@Body RegisterReqEntity registerReqEntity);

    /**
     * 注册
     */
    @POST("memberApi/login")
    Call<TokenResEntity> login(@Body LoginReqEntity loginReqEntity);
}
