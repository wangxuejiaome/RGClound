package com.rgcloud.adapter;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.response.CouponResEntity;
import com.rgcloud.entity.response.PointResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CouponAdapter extends BaseQuickAdapter<CouponResEntity.CouponBean, BaseViewHolder> {

    public CouponAdapter(List<CouponResEntity.CouponBean> data) {
        super(R.layout.item_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponResEntity.CouponBean item) {

        helper.setText(R.id.tv_name_coupon_item, item.AcitveName)
                .setText(R.id.tv_validate_coupon_item, item.StartTime + " - " + item.FinishTime);

        TextView tvActivityStatus = helper.getView(R.id.tv_status_activity_coupon_item);
        if (item.ActiveState == 0) {
            tvActivityStatus.setText("活动状态：未开始");
        } else if (item.ActiveState == 1) {
            tvActivityStatus.setText("活动状态：进行中");
        } else if (item.ActiveState == 2) {
            tvActivityStatus.setText("活动状态：已结束");
        }

        TextView tvCouponStatus = helper.getView(R.id.tv_status_coupon_item);
        if (item.TicketState == 0) {
            tvCouponStatus.setText("未使用");
        } else if (item.ActiveState == 1) {
            tvCouponStatus.setText("已使用");
        } else if (item.ActiveState == 2) {
            tvCouponStatus.setText("已过期");
        }

        final ImageView imageView = helper.getView(R.id.iv_coupon_item);
        Glide.with(AppActivityManager.getActivityManager().getCurrentActivity()).load( item.ActiveImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(AppActivityManager.getActivityManager().getCurrentActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });

        //GlideUtil.displayWithPlaceHolder(AppActivityManager.getActivityManager().getCurrentActivity(), item.ActiveImage, R.mipmap.ic_coupon_default, imageView);
    }
}
