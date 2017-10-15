package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/10/15.
 */

public class MessageResEntity extends BaseResEntity {


    /**
     * RecordCount : 1
     * CurrentPage : 2
     * DataList : [{"Type":1,"TypeCN":"sample string 2","Title":"sample string 3","Content":"sample string 4","CreateTime":"2017-10-15T20:16:04.8054727+08:00","PushTime":"sample string 6","Params":"sample string 7"},{"Type":1,"TypeCN":"sample string 2","Title":"sample string 3","Content":"sample string 4","CreateTime":"2017-10-15T20:16:04.8054727+08:00","PushTime":"sample string 6","Params":"sample string 7"}]
     */

    public int RecordCount;
    public int CurrentPage;
    /**
     * Type : 1
     * TypeCN : sample string 2
     * Title : sample string 3
     * Content : sample string 4
     * CreateTime : 2017-10-15T20:16:04.8054727+08:00
     * PushTime : sample string 6
     * Params : sample string 7
     */

    public List<MessageBean> DataList;

    public static class MessageBean {
        public int Type;
        public String TypeCN;
        public String Title;
        public String Content;
        public String CreateTime;
        public String PushTime;
        public String Params;
    }
}
