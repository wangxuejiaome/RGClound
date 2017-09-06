package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.NavigationEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class NavigationAdapter extends BaseQuickAdapter<NavigationEntity, BaseViewHolder> {

    public NavigationAdapter(List<NavigationEntity> data) {
        super(R.layout.item_navigation, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationEntity item) {

        helper.setText(R.id.tv_navigation_item, item.name);
        if (item.hasSelected) {
            helper.setTextColor(R.id.tv_navigation_item, 0xff659bc1);
        } else {
            helper.setTextColor(R.id.tv_navigation_item, 0xff333333);
        }

    }
}
