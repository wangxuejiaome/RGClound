package com.rgcloud.entity.request;

import com.rgcloud.entity.BaseResEntity;
import com.rgcloud.util.MD5Util;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RegisterRequestEntity  extends BaseResEntity{

    public String LoginPhone;
    public String ValidCode;
    private String Password;
    /**
     * Registration_Id（APP消息推送使用）
     */
    public String EquipmentId;
    /**
     * 设备类型（0-安卓，1-iOS）
     */
    public int EquipmentKind = 0;

    public void setPassword(String password) {
        Password = MD5Util.encodeByMD5(password);
    }
}
