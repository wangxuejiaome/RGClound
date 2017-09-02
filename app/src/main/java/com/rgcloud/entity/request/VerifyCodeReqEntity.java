package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class VerifyCodeReqEntity extends BaseReqEntity{

    public String PhoneNumber;
    /**
     * 短信类型： 1.注册；2.重置登录密码；3.设置支付；4.修改支付；5.重置支付；6.提现；7.绑定手机号
     */
    public int TemplateId;
}
