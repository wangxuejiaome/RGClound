package com.rgcloud.http;

import com.rgcloud.entity.request.RegisterRequestEntity;
import com.rgcloud.entity.response.TokenResEntity;

import retrofit2.Call;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RequestApi {


    /**
     * 注册
     */
    public static void register(RegisterRequestEntity registerRequestEntity,ResponseCallBack responseCallBack){
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class,false).register(registerRequestEntity);
        call.enqueue(responseCallBack);
    }


}
