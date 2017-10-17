package com.rgcloud.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.CalendarResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CalendarAdapter extends BaseQuickAdapter<ActivityResEntity.WeekDayBean, BaseViewHolder> {

    public CalendarAdapter(List<ActivityResEntity.WeekDayBean> data) {
        super(R.layout.item_calendar, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityResEntity.WeekDayBean item) {

       // helper.setText(R.id.tv_week_day_calendar, item.WeekDay);
      //  TextView tvDay = helper.getView(R.id.tv_day_calendar);
     /*   tvDay.setText(item.ShowDay + "");
        if (item.hasSelected) {
            tvDay.setTextColor(0xffffffff);
            tvDay.setBackgroundResource(R.drawable.shape_round_blue);
        } else {
            tvDay.setTextColor(0xff999999);
            tvDay.setBackgroundColor(0xffffffff);
        }*/
    }
}
