package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/10.
 */

public class GetTicketReqEntity extends BaseReqEntity {

    public int ActiveId;

    public GetTicketReqEntity(int activeId) {
        ActiveId = activeId;
    }
}
