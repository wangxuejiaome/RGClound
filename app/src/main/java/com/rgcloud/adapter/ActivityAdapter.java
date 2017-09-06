package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.ActivityResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ActivityAdapter extends BaseQuickAdapter<ActivityResEntity, BaseViewHolder> {

    public ActivityAdapter(List<ActivityResEntity> data) {
        super(R.layout.item_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityResEntity item) {

        ImageView imageView = helper.getView(R.id.iv_activity_item);

       /* helper.setText(R.id.tv_name_activity_item, item.name)
                .setText(R.id.tv_address_activity_item, item.introduce)
                .setImageResource(R.id.tv_preference_type_activity_item, item.imgRes);*/
    }
}
