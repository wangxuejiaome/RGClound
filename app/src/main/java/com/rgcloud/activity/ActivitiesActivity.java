package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.rgcloud.R.drawable.position;
import static com.tencent.qalsdk.base.a.ca;

public class ActivitiesActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.tv_title_name)
    TextView tvTitleName;
    @Bind(R.id.rv_activity_navigation)
    RecyclerView rvActivityNavigation;
    @Bind(R.id.rv_activity)
    RecyclerView rvActivity;

    private ActivityNavigationAdapter mActivityNavigationAdapter;
    private ActivityAdapter mActivityAdapter;
    private ActivityResEntity mActivityResEntity;
    private List<ActivityResEntity.ActiveTypeListBean> mActiveTypeList = new ArrayList<>();

    private int mActivityCategoryId;
    private int mSelectedActivityTypeId;
    private int mActivitySpaceId;
    private String mActivityTypeName;

    /**
     * 标识是否是志愿服务分类
     */
    private boolean mIsvoluntaryServiceType;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        rvActivityNavigation.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
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
                mSelectedActivityTypeId = mActiveTypeList.get(position).ActiveTypeId;
                if (mActiveTypeList.get(position).TypeName.contains("志愿")) {
                    mIsvoluntaryServiceType = true;
                } else {
                    mIsvoluntaryServiceType = false;
                }
                getActivities();
            }
        });

        rvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        rvActivity.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivityAdapter = new ActivityAdapter(null);
        rvActivity.setAdapter(mActivityAdapter);

        rvActivity.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityResBean activityResBean = mActivityAdapter.getItem(position);
                if (mIsvoluntaryServiceType) {
                    ActivityDetailActivity.startActivityDetail(mContext, activityResBean.ActiveId, "voluntaryServiceFlag");
                } else {
                    ActivityDetailActivity.startActivityDetail(mContext, activityResBean.ActiveId);
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
        mActivityCategoryId = getIntent().getIntExtra("activityCategoryId", 0);
        mSelectedActivityTypeId = getIntent().getIntExtra("activityTypeId", 0);
        mActivitySpaceId = getIntent().getIntExtra("activitySpaceId", 0);
        mActivityTypeName = getIntent().getStringExtra("activityTypeName");
        getActivities();
    }

    private void getActivities() {
        ActivityReqEntity activityReqEntity = new ActivityReqEntity();
        activityReqEntity.ActiveType = mActivityCategoryId;
        if (mSelectedActivityTypeId == -1) {//精彩回放
            activityReqEntity.ActiveState = 2;
        } else {
            activityReqEntity.ChildTypeId = mSelectedActivityTypeId;
        }
        activityReqEntity.SpaceId = mActivitySpaceId;
        activityReqEntity.PageIndex = mPageIndex;
        RequestApi.getActivity(activityReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mActivityResEntity = (ActivityResEntity) resEntity;

                if (mSelectedActivityTypeId == -1) {
                    tvTitleName.setText("精彩回放");
                } else {
                    if (!TextUtils.isEmpty(mActivityTypeName)) {
                        tvTitleName.setText(mActivityTypeName);
                    } else {
                        tvTitleName.setText("活动");
                    }
                }
                if (mActivitySpaceId != 0 || mSelectedActivityTypeId == -1 || (!TextUtils.isEmpty(mActivityTypeName) && mActivityTypeName.equals("文化瑰宝"))) {//文化空间、 精彩回放、文化瑰宝 不需要显示活动导航
                    rvActivityNavigation.setVisibility(View.GONE);
                } else {

                    if (mActiveTypeList.size() == 0) {
                        mActiveTypeList.addAll(mActivityResEntity.ActiveTypeList);
                        mActiveTypeList.add(0, new ActivityResEntity.ActiveTypeListBean(0, "全部", false));

                        for (int i = 0; i < mActiveTypeList.size(); i++) {
                            ActivityResEntity.ActiveTypeListBean activeTypeListBean = mActiveTypeList.get(i);
                            activeTypeListBean.hasSelected = activeTypeListBean.ActiveTypeId == mSelectedActivityTypeId;

                            if (activeTypeListBean.hasSelected && mActiveTypeList.get(i).TypeName.contains("志愿")) {
                                mIsvoluntaryServiceType = true;
                            } else {
                                mIsvoluntaryServiceType = false;
                            }
                        }

                        mActivityNavigationAdapter.setNewData(mActiveTypeList);
                    }
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

    /**
     * 启动活动界面
     *
     * @param context
     * @param activityCategoryId 1.活动（活动、活动空间） 2.资讯 3.民营剧团
     * @param activityTypeId     分类下的子分类
     */
    public static void startActivitiesActivity(Context context, int activityCategoryId, int activityTypeId, int activitySpaceId, String... args) {
        Intent intent = new Intent(context, ActivitiesActivity.class);
        intent.putExtra("activityCategoryId", activityCategoryId);
        intent.putExtra("activityTypeId", activityTypeId);
        intent.putExtra("activitySpaceId", activitySpaceId);
        if (args != null && args.length > 0){
            intent.putExtra("activityTypeName", args[0]);
        }
        context.startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
