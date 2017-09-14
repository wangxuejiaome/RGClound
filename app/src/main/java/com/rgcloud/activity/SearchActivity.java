package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.SearchAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.rv_search)
    RecyclerView rvSearch;

    SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        initTitleBar(R.id.tb_search, "搜索");

        rvSearch.setLayoutManager(new LinearLayoutManager(mContext));
        rvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.y20).build());
        mSearchAdapter = new SearchAdapter(R.layout.item_search, null);
        rvSearch.setAdapter(mSearchAdapter);

        rvSearch.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                String searchKey = (String) adapter.getItem(position);
                Intent searchResultIntent = new Intent(mContext, SearchResultActivity.class);
                searchResultIntent.putExtra("searchKey", searchKey);
                startActivity(searchResultIntent);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        Intent searchResultIntent = new Intent(mContext, SearchResultActivity.class);
                        searchResultIntent.putExtra("searchKey", etSearch.getText().toString().trim());
                        startActivity(searchResultIntent);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initData() {
        List<String> hotSearchKeyList = (List<String>) getIntent().getSerializableExtra("hotSearchKeys");
        mSearchAdapter.setNewData(hotSearchKeyList);
    }

    @OnClick({R.id.iv_clear_search_result, R.id.iv_search})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_clear_search_result:
                etSearch.setText("");
                break;
            case R.id.iv_search:
                break;
        }
    }
}
