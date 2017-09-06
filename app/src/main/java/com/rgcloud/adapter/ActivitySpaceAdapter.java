package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ActivitySpaceAdapter extends BaseQuickAdapter<ActivitySpaceResEntity, BaseViewHolder> {

    public ActivitySpaceAdapter(List<ActivitySpaceResEntity> data) {
        super(R.layout.item_activity_space, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivitySpaceResEntity item) {

        ImageView imageView = helper.getView(R.id.iv_activity_space_item);

        /*elper.setText(R.id.tv_name_activity_space_item, item.name)
                .setText(R.id.tv_address_activity_space_item, item.introduce)
                .setImageResource(R.id.tv_count_activity_space_item, item.imgRes);*/
    }
}
