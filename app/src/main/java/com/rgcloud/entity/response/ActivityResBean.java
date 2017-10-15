package com.rgcloud.entity.response;

/**
 * Created by wangxuejiao on 2017/9/9.
 */

public class ActivityResBean {

    public int ActiveId;
    public int SpaceId;
    public String ActiveName;
    public String SpaceName;
    public String Address;
    public String ActiveImage;
    public String SpaceImage;
    public int IsNeedTicket;

    /**
     * 0-原来的活动详情，1-新的详情
     */
    public int IsOrNotNewPage;

}
