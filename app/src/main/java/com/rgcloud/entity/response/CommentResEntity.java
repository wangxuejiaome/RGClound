package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/24.
 */

public class CommentResEntity extends BaseResEntity {


    /**
     * CommentTime : 2017-09-24T11:16:19.508147+08:00
     * CommentTime_Show : sample string 2
     * Content : sample string 3
     * ActiveImage : sample string 4
     * ActiveName : sample string 5
     */

    public List<CommentBean> CommentList;

    public static class CommentBean {
        public String CommentTime;
        public String CommentTime_Show;
        public String Content;
        public String ActiveImage;
        public String ActiveName;

    }
}
