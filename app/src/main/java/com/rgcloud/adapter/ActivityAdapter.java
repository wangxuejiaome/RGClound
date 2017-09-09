package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ActivityAdapter extends BaseQuickAdapter<ActivityResBean, BaseViewHolder> {

    public ActivityAdapter(List<ActivityResBean> data) {
        super(R.layout.item_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityResBean item) {


        helper.setText(R.id.tv_name_activity_item, item.ActiveName)
                .setText(R.id.tv_address_activity_item, item.Address);

        ImageView imageView = helper.getView(R.id.iv_activity_item);
        GlideUtil.displayWithPlaceHolder(AppActivityManager.getActivityManager().getCurrentActivity(),item.ActiveImage,R.mipmap.activity_temp,imageView);
        if(item.IsNeedTicket == 0){
            helper.setText(R.id.tv_preference_type_activity_item,"免费");
        }else {
            helper.setText(R.id.tv_preference_type_activity_item,"送票");
        }
    }
}
