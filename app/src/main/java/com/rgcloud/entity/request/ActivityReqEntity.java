package com.rgcloud.entity.request;

import com.rgcloud.config.Constant;

/**
 * Created by wangxuejiao on 2017/9/9.
 */

public class ActivityReqEntity extends BaseReqEntity {


    /**
     * ActiveType : 1
     * ChildTypeId : 2
     * SpaceId : 3
     * ActiveName : sample string 4
     * PageIndex : 5
     * PageSize : 6r
     * IsCalendar : 7
     * Day : 8
     */

    public int ActiveType;
    public int ChildTypeId;
    public int SpaceId;
    public String ActiveName;
    public int PageIndex;
    public int PageSize = Constant.DEFAULT_PAGE_SIZE;
    public int IsCalendar;
    public Integer Day;
}
