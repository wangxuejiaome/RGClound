package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/7.
 */

public class ActivitySpaceResEntity extends BaseResEntity {

    public List<SpaceTypeBean> TypeData;
    public List<ActivitySpaceBean> DataList;

    public static class SpaceTypeBean {

        public String TypeName;
        public int TypeId;
        public boolean hasSelected;
    }

    /**
     * Id : 1
     * SpaceName : sample string 2
     * SpaceAddress : sample string 3
     * TotalActive : 4
     * OnLineActive : 5
     * Longitude : 6.0
     * Latitude : 7.0
     * IsValid : 8
     * CreateTime : 2017-09-10T15:54:59.7639675+08:00
     * SpaceImage : sample string 10
     * SpaceInfo : sample string 11
     * SpacePhone : sample string 12
     * SpaceType : 13
     */

    public static class ActivitySpaceBean {
        public int Id;
        public String SpaceName;
        public String SpaceAddress;
        public int TotalActive;
        public int OnLineActive;
        public double Longitude;
        public double Latitude;
        public int IsValid;
        public String CreateTime;
        public String SpaceImage;
        public String SpaceInfo;
        public String SpacePhone;
        public int SpaceType;
    }
}
