package com.rgcloud.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.CollectionAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.response.CollectionResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;

public class CollectionActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_collection)
    RecyclerView rvCollection;

    private CollectionAdapter mCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        initTitleBar(R.id.tb_collection, "我的收藏");

        rvCollection.setLayoutManager(new LinearLayoutManager(mContext));
        rvCollection.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.y10).showLastDivider().build());
        mCollectionAdapter = new CollectionAdapter(null);
        rvCollection.setAdapter(mCollectionAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getCollection();
            }
        });

        rvCollection.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionResEntity.CollectionBean collectionBean = (CollectionResEntity.CollectionBean) adapter.getItem(position);
                ActivityDetailActivity.startActivityDetail(mContext, collectionBean.ActiveId);
            }
        });
    }

    private void initData() {
        getCollection();
    }


    public void getCollection() {
        RequestApi.getCollection(new BaseReqEntity(), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                CollectionResEntity collectionResEntity = (CollectionResEntity) resEntity;
                mCollectionAdapter.setNewData(collectionResEntity.MemberCollect);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
            }
        });
    }

    @OnClick({R.id.btn_left_include_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_include_title:
                finish();
                break;
        }
    }
}
