package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/23.
 */

public class PointResEntity extends BaseResEntity{


    /**
     * RecordCount : 1
     * PageIndex : 2
     * RecordList : [{"Id":1,"Score":2,"TrackType":"sample string 3","TrackTime":"sample string 4","TrackRemark":"sample string 5"},{"Id":1,"Score":2,"TrackType":"sample string 3","TrackTime":"sample string 4","TrackRemark":"sample string 5"}]
     */

    public int RecordCount;
    public int PageIndex;
    /**
     * Id : 1
     * Score : 2
     * TrackType : sample string 3
     * TrackTime : sample string 4
     * TrackRemark : sample string 5
     */

    public List<PointBean> RecordList;

    

    public static class PointBean {
        public int Id;
        public int Score;
        public String TrackType;
        public String TrackTime;
        public String TrackRemark;
        
    }
}
