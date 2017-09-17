package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class SpaceMapNavigationAdapter extends BaseQuickAdapter<ActivitySpaceResEntity.SpaceTypeBean, BaseViewHolder> {

    public SpaceMapNavigationAdapter(List<ActivitySpaceResEntity.SpaceTypeBean> data) {
        super(R.layout.item_navigation, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivitySpaceResEntity.SpaceTypeBean item) {

        helper.setText(R.id.tv_navigation_item, item.TypeName);
        if (item.hasSelected) {
            helper.setTextColor(R.id.tv_navigation_item, 0xff659bc1);
        } else {
            helper.setTextColor(R.id.tv_navigation_item, 0xff333333);
        }
    }
}
