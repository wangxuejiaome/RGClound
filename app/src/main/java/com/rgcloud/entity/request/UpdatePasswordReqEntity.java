package com.rgcloud.entity.request;

import com.rgcloud.util.MD5Util;

/**
 * Created by wangxuejiao on 2017/9/3.
 */

public class UpdatePasswordReqEntity extends BaseReqEntity {

    private String OldPassword;
    private String NewPassword;

    public void setOldPassword(String OldPassword) {
        this.OldPassword = MD5Util.encodeByMD5(OldPassword);
    }

    public void setNewPassword(String NewPassword) {
        this.NewPassword = MD5Util.encodeByMD5(NewPassword);
    }
}
