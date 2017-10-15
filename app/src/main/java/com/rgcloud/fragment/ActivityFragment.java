package com.rgcloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.activity.ActivitiesActivity;
import com.rgcloud.activity.ActivityDetailActivity;
import com.rgcloud.activity.InformationDetailActivity;
import com.rgcloud.activity.Main2Activity;
import com.rgcloud.activity.SearchActivity;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.ActivityNavigationAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import java.io.Serializable;
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

public class ActivityFragment extends Fragment {


    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_activity_navigation)
    RecyclerView rvActivityNavigation;
    @Bind(R.id.rv_activity)
    RecyclerView rvActivity;

    private ActivityNavigationAdapter mActivityNavigationAdapter;
    private ActivityAdapter mActivityAdapter;
    private ActivityResEntity mActivityResEntity;
    private List<ActivityResEntity.ActiveTypeListBean> mActiveTypeList = new ArrayList<>();

    private int mSelectedTypeId;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
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
        rvActivityNavigation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mActivityNavigationAdapter = new ActivityNavigationAdapter(mActiveTypeList);
        rvActivityNavigation.setAdapter(mActivityNavigationAdapter);

        rvActivityNavigation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < mActiveTypeList.size(); i++) {
                    ActivityResEntity.ActiveTypeListBean activeTypeListBean = mActiveTypeList.get(i);
                    activeTypeListBean.hasSelected = i == position;
                }
                mActivityNavigationAdapter.notifyDataSetChanged();
                mSelectedTypeId = mActiveTypeList.get(position).ActiveTypeId;
                getActivities();
            }
        });

        rvActivity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvActivity.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivityAdapter = new ActivityAdapter(null);
        rvActivity.setAdapter(mActivityAdapter);

        rvActivity.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityResBean activityResBean = mActivityAdapter.getItem(position);
                if (activityResBean.IsOrNotNewPage == 0) {
                    ActivityDetailActivity.startActivityDetail(getActivity(),activityResBean.ActiveId);
                } else {
                    InformationDetailActivity.startActivityDetail(getActivity(), activityResBean.ActiveId);
                }
            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                getActivities();
            }
        });

        mActivityAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvActivity.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsEnd) {
                            mActivityAdapter.loadMoreEnd(true);
                            ToastUtil.showShortToast("没有更多了");
                        } else {
                            mPageIndex++;
                            getActivities();
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        getActivities();
    }

    private void getActivities() {
        ActivityReqEntity activityReqEntity = new ActivityReqEntity();
        activityReqEntity.ActiveType = 1;
        activityReqEntity.ChildTypeId = mSelectedTypeId;
        RequestApi.getActivity(activityReqEntity, new ResponseCallBack(getActivity()) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mActivityResEntity = (ActivityResEntity) resEntity;
                if (mActiveTypeList.size() == 0) {
                    mActiveTypeList.addAll(mActivityResEntity.ActiveTypeList);
                    mActiveTypeList.add(0, new ActivityResEntity.ActiveTypeListBean(0, "全部", true));
                    mActivityNavigationAdapter.setNewData(mActiveTypeList);
                }

                if (mPageIndex == 1) {
                    mActivityAdapter.setNewData(mActivityResEntity.ActiveList);
                    ptrClassicFrameLayout.refreshComplete();
                    mActivityAdapter.disableLoadMoreIfNotFullPage(rvActivity);
                } else {
                    mActivityAdapter.addData(mActivityResEntity.ActiveList);
                    mActivityAdapter.loadMoreComplete();
                }

                mIsEnd = mActivityResEntity.ActiveList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mActivityAdapter.getItemCount() == 0) {
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

    @OnClick({R.id.iv_search, R.id.rv_activity_navigation, R.id.rv_activity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                Main2Activity main2Activity = (Main2Activity) getActivity();
                if (main2Activity.getSearchKey() != null) {
                    Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                    searchIntent.putExtra("hotSearchKeys", (Serializable) main2Activity.getSearchKey());
                    startActivity(searchIntent);
                }
                break;
            case R.id.rv_activity_navigation:
                break;
            case R.id.rv_activity:
                break;
        }
    }
}
