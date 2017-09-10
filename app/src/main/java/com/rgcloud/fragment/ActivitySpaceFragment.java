package com.rgcloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.activity.ActivityDetailActivity;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.ActivitySpaceAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class ActivitySpaceFragment extends Fragment {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.rv_activity_space)
    RecyclerView rvActivitySpace;

    private ActivitySpaceAdapter mActivitySpaceAdapter;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_space, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        rvActivitySpace.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvActivitySpace.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivitySpaceAdapter = new ActivitySpaceAdapter(null);
        rvActivitySpace.setAdapter(mActivitySpaceAdapter);

        rvActivitySpace.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
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
        final BaseReqEntity baseReqEntity = new BaseReqEntity();
        RequestApi.getActivitySpace(baseReqEntity, new ResponseCallBack(getActivity()) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
    }
}
