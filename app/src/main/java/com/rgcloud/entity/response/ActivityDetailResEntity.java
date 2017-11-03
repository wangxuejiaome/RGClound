package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

/**
 * Created by wangxuejiao on 2017/9/10.
 */

public class ActivityDetailResEntity extends BaseResEntity {


    /**
     * ActiveId : 1
     * ActiveName : sample string 2
     * ActiveImage : sample string 3
     * ActiveAddress : sample string 4
     * Longitude : 5.0
     * Latitude : 6.0
     * ActiveTime : sample string 7
     * ConnectPhone : sample string 8
     * IsNeedTicket : 9
     * ActiveState : 10
     * ShareSendScore : 11
     * CommentSendScore : 12
     */

    public int ActiveId;
    public String ActiveName;
    public String ActiveImage;
    public String ActiveAddress;
    public double Longitude;
    public double Latitude;
    public String ActiveTime;
    public String ConnectPhone;
    public int IsNeedTicket;
    public int ActiveState;
    public int ShareSendScore;
    public int CommentSendScore;
    public String DetailUrl;
    public String SharedUrl;
    public int RemaindTicket;
    
}
