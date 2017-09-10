package com.rgcloud.entity.request;

/**
 * Created by wangxuejiao on 2017/9/10.
 */

public class CollectCancelReqEntity extends BaseReqEntity {

    public int ActiveId;

    public CollectCancelReqEntity(int activeId) {
        ActiveId = activeId;
    }
}
