package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.HomeResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class FunctionNavigationAdapter extends BaseQuickAdapter<FunctionEntity, BaseViewHolder> {

    public FunctionNavigationAdapter(List<FunctionEntity> data) {
        super(R.layout.item_function_navigation, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionEntity  item) {

        helper.setText(R.id.tv_name_function_navigation_item,item.name);
        helper.setImageResource(R.id.iv_function_navigation_item,item.imgRes);

       // ImageView imageView = helper.getView(R.id.iv_function_navigation_item);
       // GlideUtil.displayNoPlaceHolde(AppActivityManager.getActivityManager().getCurrentActivity(), item.ImageUrl, imageView);
    }
}
