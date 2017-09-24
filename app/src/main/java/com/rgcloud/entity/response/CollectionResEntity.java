package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/24.
 */

public class CollectionResEntity extends BaseResEntity {


    /**
     * ActiveId : 1
     * ActiveName : sample string 2
     * ActiveImage : sample string 3
     * StartTime : sample string 4
     * FinishTime : sample string 5
     * SpaceAddress : sample string 6
     */

    public List<CollectionBean> MemberCollect;

    public static class CollectionBean {
        public int ActiveId;
        public String ActiveName;
        public String ActiveImage;
        public String StartTime;
        public String FinishTime;
        public String SpaceAddress;
    }
}
