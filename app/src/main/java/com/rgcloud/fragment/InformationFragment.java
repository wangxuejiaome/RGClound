package com.rgcloud.fragment;

import android.app.Fragment;
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
import com.rgcloud.adapter.InformationAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class InformationFragment extends Fragment {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_information)
    RecyclerView rvInformation;

    private InformationAdapter mInformationAdapter;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
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
        rvInformation.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvInformation.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        mInformationAdapter = new InformationAdapter(null);
        rvInformation.setAdapter(mInformationAdapter);

        rvInformation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //ActivityResBean activityResBean = mActivitySpaceAdapter.getItem(position);
                //ActivityDetailActivity.startActivityDetail(getActivity(),activityResBean.ActiveId);
            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                getActivities();
            }
        });

        mInformationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvInformation.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsEnd) {
                            mInformationAdapter.loadMoreEnd(true);
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
        activityReqEntity.ActiveType = 2;
        RequestApi.getActivity(activityReqEntity, new ResponseCallBack(getActivity()) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                ActivityResEntity activityResEntity = (ActivityResEntity) resEntity;
                if (mPageIndex == 1) {
                    mInformationAdapter.setNewData(activityResEntity.ActiveList);
                    ptrClassicFrameLayout.refreshComplete();
                    mInformationAdapter.disableLoadMoreIfNotFullPage(rvInformation);
                } else {
                    mInformationAdapter.addData(activityResEntity.ActiveList);
                    mInformationAdapter.loadMoreComplete();
                }

                mIsEnd = activityResEntity.ActiveList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mInformationAdapter.getItemCount() == 0) {
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

}
