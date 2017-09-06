package com.rgcloud.entity;

/**
 * Created by wangxuejiao on 2017/9/7.
 */

public class NavigationEntity {

    public String name;
    public int id;
    public boolean hasSelected;

    public NavigationEntity(String name,boolean hasSelected) {
        this.name = name;
        this.hasSelected = hasSelected;
    }
}
