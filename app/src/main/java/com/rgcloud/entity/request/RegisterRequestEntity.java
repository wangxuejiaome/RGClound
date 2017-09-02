package com.rgcloud.entity.request;

import com.rgcloud.entity.BaseResEntity;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RegisterRequestEntity  extends BaseResEntity{

    private String LoginPhone;
    private String ValidCode;
    private String Password;
    /**
     * Registration_Id（APP消息推送使用）
     */
    private String EquipmentId;
    /**
     * 设备类型（0-安卓，1-iOS）
     */
    private int EquipmentKind;
}
