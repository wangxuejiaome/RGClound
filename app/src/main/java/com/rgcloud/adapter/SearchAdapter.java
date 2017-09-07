package com.rgcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rgcloud.R;

import java.util.List;


/**
 * Created by play on 2016/12/30.
 */

public class SearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String searchKey) {
        baseViewHolder.setText(R.id.tv_keywords_search_item, searchKey);
    }
}
