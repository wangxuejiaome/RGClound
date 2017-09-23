package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.entity.response.PointResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class PointAdapter extends BaseQuickAdapter<PointResEntity.PointBean, BaseViewHolder> {

    public PointAdapter(List<PointResEntity.PointBean> data) {
        super(R.layout.item_point, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointResEntity.PointBean item) {

        helper.setText(R.id.tv_title_point_item, item.TrackType)
                .setText(R.id.tv_description_point_item, item.TrackRemark)
                .setText(R.id.tv_count_point_item, "+" + item.Score)
                .setText(R.id.tv_date_point_item, item.TrackTime);
    }
}
