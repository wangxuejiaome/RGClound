package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/17.
 */

public class PointReqEntity extends BaseReqEntity {

    public PointReqEntity(int pageIndex, int pageSize) {
        PageIndex = pageIndex;
        PageSize = pageSize;
    }

    public int PageIndex;
    public int PageSize;
}
