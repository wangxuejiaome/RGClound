package com.rgcloud.entity.request;

import com.rgcloud.util.MD5Util;

/**
 * Created by wangxuejiao on 2017/9/3.
 */

public class LoginReqEntity extends BaseReqEntity {
    /**
     * LoginPhone : sample string 1
     * LoginPassword : sample string 2
     * EquipmentId : sample string 3
     * EquipmentKind : 4
     * LoginType : 5
     */

    public String LoginPhone;
    private String LoginPassword;
    /**
     * Registration_Id（APP消息推送使用）
     */
    public String EquipmentId;
    /**
     * 设备类型（0-安卓，1-iOS）
     */
    public int EquipmentKind = 0;
    /**
     * 登陆模式（0-常规登陆，1-强制登陆）
     */
    public int LoginType = 0;

    public void setPassword(String password) {
        LoginPassword = MD5Util.encodeByMD5(password);
    }

}
