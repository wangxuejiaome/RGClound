package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.CollectionResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CollectionAdapter extends BaseQuickAdapter<CollectionResEntity.CollectionBean, BaseViewHolder> {

    public CollectionAdapter(List<CollectionResEntity.CollectionBean> data) {
        super(R.layout.item_collection, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionResEntity.CollectionBean item) {

        helper.setText(R.id.tv_title_collection_item, item.ActiveName)
                .setText(R.id.tv_date_collection_item, item.StartTime + " - " + item.FinishTime)
                .setText(R.id.tv_address_collection_item, item.SpaceAddress);

        ImageView imageView = helper.getView(R.id.iv_collection);
        GlideUtil.displayWithPlaceHolder(AppActivityManager.getActivityManager().getCurrentActivity(), item.ActiveImage, R.mipmap.activity_temp, imageView);
    }
}
