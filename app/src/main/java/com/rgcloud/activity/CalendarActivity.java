package com.rgcloud.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.CalendarAdapter;
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

public class CalendarActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.tv_calendar)
    TextView tvCalendar;
    @Bind(R.id.rv_calendar)
    RecyclerView rvCalendar;
    @Bind(R.id.rv_calendar_activity)
    RecyclerView rvCalendarActivity;

    private ActivityAdapter mActivityAdapter;
    private CalendarAdapter mCalendarAdapter;
    List<ActivityResEntity.WeekDayBean> mWeekDayBeanList = new ArrayList<>();
    private ActivityResEntity mActivityResEntity;

    private Integer mSelectedDay;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        rvCalendar.setLayoutManager(new GridLayoutManager(mContext, 7));
        mCalendarAdapter = new CalendarAdapter(mWeekDayBeanList);
        rvCalendar.setAdapter(mCalendarAdapter);

        rvCalendar.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < mWeekDayBeanList.size(); i++) {
                    ActivityResEntity.WeekDayBean weekDayBean = mWeekDayBeanList.get(i);
                    weekDayBean.hasSelected = i == position;
                }
                mCalendarAdapter.notifyDataSetChanged();
                mSelectedDay = mWeekDayBeanList.get(position).PostDay;
                getActivities();
            }
        });

        rvCalendarActivity.setLayoutManager(new LinearLayoutManager(mContext));
        rvCalendarActivity.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivityAdapter = new ActivityAdapter(null);
        rvCalendarActivity.setAdapter(mActivityAdapter);

        rvCalendarActivity.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityResBean activityResBean = mActivityAdapter.getItem(position);
                ActivityDetailActivity.startActivityDetail(mContext, activityResBean.ActiveId);
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
                rvCalendarActivity.post(new Runnable() {
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
        activityReqEntity.IsCalendar = 1;
        activityReqEntity.Day = mSelectedDay;
        RequestApi.getActivity(activityReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mActivityResEntity = (ActivityResEntity) resEntity;
                if (mWeekDayBeanList.size() == 0) {
                    mWeekDayBeanList.addAll(mActivityResEntity.WeekDay);
                    mWeekDayBeanList.get(3).hasSelected = true;
                    mCalendarAdapter.setNewData(mWeekDayBeanList);

                    String postDay = String.valueOf(mWeekDayBeanList.get(3).PostDay);
                    String year = postDay.substring(0,4);
                    String month = postDay.substring(4,6);
                    tvCalendar.setText(year + "." + month);
                }

                if (mPageIndex == 1) {
                    mActivityAdapter.setNewData(mActivityResEntity.ActiveList);
                    ptrClassicFrameLayout.refreshComplete();
                    mActivityAdapter.disableLoadMoreIfNotFullPage(rvCalendarActivity);
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

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
