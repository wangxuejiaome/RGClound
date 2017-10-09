package com.rgcloud.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.ActivitySpaceAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.SpaceReqEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class SpaceActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_activity_space)
    RecyclerView rvActivitySpace;

    private ActivitySpaceAdapter mActivitySpaceAdapter;
    private int mPageIndex = 1;
    public boolean mIsEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        initView();
        initData();
    }

    private void initView() {
        rvActivitySpace.setLayoutManager(new LinearLayoutManager(mContext));
        rvActivitySpace.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivitySpaceAdapter = new ActivitySpaceAdapter(null);
        rvActivitySpace.setAdapter(mActivitySpaceAdapter);

        rvActivitySpace.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivitySpaceResEntity.ActivitySpaceBean activitySpaceBean = (ActivitySpaceResEntity.ActivitySpaceBean) adapter.getItem(position);
                ActivitiesActivity.startActivitiesActivity(mContext, 1, 0, activitySpaceBean.Id);
                //ActivityResBean activityResBean = mActivitySpaceAdapter.getItem(position);
                // ActivityDetailActivity.startActivityDetail(getActivity(),activityResBean.ActiveId);
            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                getActivitySpace();
            }
        });

        mActivitySpaceAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvActivitySpace.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mIsEnd) {
                            mActivitySpaceAdapter.loadMoreEnd(true);
                            ToastUtil.showShortToast("没有更多了");
                        } else {
                            mPageIndex++;
                            getActivitySpace();
                        }
                    }
                });
            }
        });

    }

    private void initData() {
        getActivitySpace();
    }

    private void getActivitySpace() {
        final SpaceReqEntity spaceReqEntity = new SpaceReqEntity();
        RequestApi.getActivitySpace(spaceReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                ActivitySpaceResEntity activitySpaceResEntity = (ActivitySpaceResEntity) resEntity;
                if (mPageIndex == 1) {
                    mActivitySpaceAdapter.setNewData(activitySpaceResEntity.DataList);
                    ptrClassicFrameLayout.refreshComplete();
                    mActivitySpaceAdapter.disableLoadMoreIfNotFullPage(rvActivitySpace);
                } else {
                    mActivitySpaceAdapter.addData(activitySpaceResEntity.DataList);
                    mActivitySpaceAdapter.loadMoreComplete();
                }

                mIsEnd = activitySpaceResEntity.DataList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mActivitySpaceAdapter.getItemCount() == 0) {
                    ToastUtil.showShortToast("暂无数据");
                }
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
