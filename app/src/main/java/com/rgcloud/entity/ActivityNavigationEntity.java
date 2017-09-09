package com.rgcloud.entity;

/**
 * Created by wangxuejiao on 2017/9/7.
 */

public class ActivityNavigationEntity {

    public String name;
    public int id;
    public boolean hasSelected;

    public ActivityNavigationEntity(String name, boolean hasSelected) {
        this.name = name;
        this.hasSelected = hasSelected;
    }
}
