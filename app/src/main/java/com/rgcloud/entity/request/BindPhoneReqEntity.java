package com.rgcloud.entity.request;

import com.rgcloud.util.MD5Util;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class BindPhoneReqEntity extends BaseReqEntity {

    public String PhoneNumber;
    public String ValidCode;
    private String NewPassword;

    public void setPassword(String password) {
        NewPassword = MD5Util.encodeByMD5(password);
    }
}
