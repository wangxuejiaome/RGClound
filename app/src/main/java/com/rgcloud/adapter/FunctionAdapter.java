package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.HomeResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class FunctionAdapter extends BaseQuickAdapter<FunctionEntity, BaseViewHolder> {

    public FunctionAdapter(List<FunctionEntity> data) {
        super(R.layout.item_function, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionEntity item) {

        helper.setText(R.id.tv_name_function_item, item.name)
                .setText(R.id.tv_introduction_function_item, item.introduce)
                .setImageResource(R.id.iv_function_item, item.imgRes);
    }
}
