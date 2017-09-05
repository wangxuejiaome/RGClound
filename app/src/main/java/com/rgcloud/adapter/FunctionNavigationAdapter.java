package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.HomeResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class FunctionNavigationAdapter extends BaseQuickAdapter<HomeResEntity, BaseViewHolder> {

    public FunctionNavigationAdapter(List<HomeResEntity> data) {
        super(R.layout.item_function_navigation, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeResEntity item) {

        //helper.setText(R.id.tv_name_function_navigation_item,);
        ImageView imageView = helper.getView(R.id.iv_function_navigation_item);
    }
}
