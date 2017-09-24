package com.rgcloud.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.response.CollectionResEntity;
import com.rgcloud.entity.response.CommentResEntity;
import com.rgcloud.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CommentAdapter extends BaseQuickAdapter<CommentResEntity.CommentBean, BaseViewHolder> {

    public CommentAdapter(List<CommentResEntity.CommentBean> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentResEntity.CommentBean item) {

        helper.setText(R.id.tv_title_comment_item, item.ActiveName)
                .setText(R.id.tv_content_collection_item, item.Content)
                .setText(R.id.tv_date_comment_item, item.CommentTime_Show);

        ImageView imageView = helper.getView(R.id.iv_comment);
        GlideUtil.displayWithPlaceHolder(AppActivityManager.getActivityManager().getCurrentActivity(), item.ActiveImage, R.mipmap.activity_temp, imageView);
    }
}
