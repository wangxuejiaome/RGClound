package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/17.
 */

public class MessageReqEntity extends BaseReqEntity {

    public MessageReqEntity(int pageIndex, int pageSize) {
        PageIndex = pageIndex;
        PageSize = pageSize;
    }

    public int PageIndex;
    public int PageSize;
}
