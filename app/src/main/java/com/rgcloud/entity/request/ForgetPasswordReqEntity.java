package com.rgcloud.entity.request;

import com.rgcloud.util.MD5Util;

/**
 * Created by wangxuejiao on 2017/9/3.
 */

public class ForgetPasswordReqEntity extends BaseReqEntity {

    public String PhoneNumber;
    public String ValidCode;
    private String NewPassword;

    public void setNewPassword(String NewPassword) {
        this.NewPassword = MD5Util.encodeByMD5(NewPassword);
    }
}
