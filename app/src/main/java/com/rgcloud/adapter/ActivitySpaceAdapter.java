package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ActivitySpaceAdapter extends BaseQuickAdapter<ActivitySpaceResEntity.ActivitySpaceBean, BaseViewHolder> {

    public ActivitySpaceAdapter(List<ActivitySpaceResEntity.ActivitySpaceBean> data) {
        super(R.layout.item_activity_space, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivitySpaceResEntity.ActivitySpaceBean item) {

        helper.setText(R.id.tv_name_activity_space_item, item.SpaceName)
                .setText(R.id.tv_address_activity_space_item, item.SpaceAddress)
                .setText(R.id.tv_count_activity_space_item, item.TotalActive + "在线活动");

        ImageView imageView = helper.getView(R.id.iv_activity_space_item);
        GlideUtil.displayWithPlaceHolder(AppActivityManager.getActivityManager().getCurrentActivity(),item.SpaceImage,R.mipmap.activity_temp,imageView);
    }
}
