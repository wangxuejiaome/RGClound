package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;
import com.rgcloud.entity.response.MessageResEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageResEntity.MessageBean, BaseViewHolder> {

    public MessageAdapter(List<MessageResEntity.MessageBean> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageResEntity.MessageBean item) {

        helper.setText(R.id.tv_title_message_item, item.Title)
                .setText(R.id.tv_time_message_item, item.PushTime)
                .setText(R.id.tv_content_message_item, item.Content);
    }
}
