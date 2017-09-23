package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/23.
 */

public class CouponResEntity extends BaseResEntity {


    /**
     * AcitveName : sample string 1
     * StartTime : sample string 2
     * FinishTime : sample string 3
     * TicketUrl : sample string 4
     * ActiveImage : sample string 5
     * ActiveState : 6
     * TicketState : 7
     */

    public List<CouponBean> MemberTickets;

    public static class CouponBean {
        public String AcitveName;
        public String StartTime;
        public String FinishTime;
        public String TicketUrl;
        public String ActiveImage;
        /**
         * 活动状态：0未开;1进行中;2已结束
         */
        public int ActiveState;
        /**
         * 0.未使用; 1.已使用; 2已过期
         */
        public int TicketState;
    }
}
