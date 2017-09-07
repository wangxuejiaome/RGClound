package com.rgcloud.http;

import com.rgcloud.entity.BaseResEntity;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.request.ForgetPasswordReqEntity;
import com.rgcloud.entity.request.LoginReqEntity;
import com.rgcloud.entity.request.RegisterReqEntity;
import com.rgcloud.entity.request.UpdatePasswordReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;
import com.umeng.socialize.media.Base;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RequestApi {

    /**
     * 获取验证码
     */
    public static void getVerifyCode(VerifyCodeReqEntity verifyCodeReqEntity, ResponseCallBack responseCallBack) {
        Call<VerifyCodeResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getVerifyCode(verifyCodeReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 注册
     */
    public static void register(RegisterReqEntity registerReqEntity, ResponseCallBack responseCallBack) {
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).register(registerReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 登录
     */
    public static void login(LoginReqEntity loginReqEntity, ResponseCallBack responseCallBack) {
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).login(loginReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 忘记密码
     */
    public static void forgetPassword(ForgetPasswordReqEntity forgetPasswordReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).forgetPassword(forgetPasswordReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 修改密码
     */
    public static void updatePassword(UpdatePasswordReqEntity updatePasswordReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).updatePassword(updatePasswordReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 退出登录
     */
    public static void loginOut(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).loginOut(baseReqEntity);
        call.enqueue(responseCallBack);
    }


}
