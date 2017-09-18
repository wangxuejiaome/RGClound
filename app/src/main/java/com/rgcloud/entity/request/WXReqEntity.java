package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/18.
 */

public class WXReqEntity extends BaseReqEntity {


    /**
     * OpenId : sample string 1
     * UnionId : sample string 2
     * WeChatNickName : sample string 3
     * LogoUrl : sample string 4
     * EquipmentId : sample string 5
     * EquipmentKind : 6
     * LoginType : 7
     */

    public String OpenId;
    public String UnionId;
    public String WeChatNickName;
    public String LogoUrl;
    public String EquipmentId;
    public int EquipmentKind;
    public int LoginType;
}
