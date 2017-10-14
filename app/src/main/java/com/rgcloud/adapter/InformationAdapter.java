package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.ActivityResBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class InformationAdapter extends BaseQuickAdapter<ActivityResBean, BaseViewHolder> {

    public InformationAdapter(List<ActivityResBean> data) {
        super(R.layout.item_information, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityResBean item) {


        helper.setText(R.id.tv_title_information_item, item.ActiveName)
                .setText(R.id.tv_address_information_item,item.Address);

    }
}
