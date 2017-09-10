package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/6.
 */

public class ActivityResEntity extends BaseResEntity {


    /**
     * sample string 1 : 2
     * sample string 3 : 4
     */

    public List<WeekDayBean> WeekDay;

    /**
     * ActiveTypeList : [{"ActiveTypeId":1,"TypeName":"sample string 2","IsChoosed":3},{"ActiveTypeId":1,"TypeName":"sample string 2","IsChoosed":3}]
     * ActiveList : [{"ActiveId":1,"SpaceId":2,"ActiveName":"sample string 3","SpaceName":"sample string 4","Address":"sample string 5","ActiveImage":"sample string 6","SpaceImage":"sample string 7","IsNeedTicket":8},{"ActiveId":1,"SpaceId":2,"ActiveName":"sample string 3","SpaceName":"sample string 4","Address":"sample string 5","ActiveImage":"sample string 6","SpaceImage":"sample string 7","IsNeedTicket":8}]
     * PageIndex : 1
     * NCount : 2
     */

    public class WeekDayBean {
        public String WeekDay;
        public int ShowDay;
        public int PostDay;
        public boolean hasSelected;
    }

    public int PageIndex;
    public int NCount;
    /**
     * ActiveTypeId : 1
     * TypeName : sample string 2
     * IsChoosed : 3
     */

    public List<ActiveTypeListBean> ActiveTypeList;
    /**
     * ActiveId : 1
     * SpaceId : 2
     * ActiveName : sample string 3
     * SpaceName : sample string 4
     * Address : sample string 5
     * ActiveImage : sample string 6
     * SpaceImage : sample string 7
     * IsNeedTicket : 8
     */

    public List<ActivityResBean> ActiveList;

    public static class ActiveTypeListBean {
        public int ActiveTypeId;
        public String TypeName;
        public boolean hasSelected;

        public ActiveTypeListBean(int activeTypeId, String typeName, boolean hasSelected) {
            ActiveTypeId = activeTypeId;
            TypeName = typeName;
            this.hasSelected = hasSelected;
        }
    }

}
