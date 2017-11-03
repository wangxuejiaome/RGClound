package com.rgcloud.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.rgcloud.R;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.ActivityDaysReqEntity;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.response.ActivityDaysResEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends BaseActivity implements OnCalendarChangedListener {

    @Bind(R.id.tv_calendar)
    TextView tvCalendar;
    @Bind(R.id.calendar)
    NCalendar calendar;

    @Bind(R.id.rv_calendar_activity)
    RecyclerView rvCalendarActivity;

    private ActivityAdapter mActivityAdapter;
    private ActivityResEntity mActivityResEntity;
    private Integer mSelectMonth;
    private Integer mSelectedDay;

    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        calendar.setOnCalendarChangedListener(this);
        initView();
        initData();
    }


    private void initView() {

        Calendar c = Calendar.getInstance();  //获取Calendar的方法
        int month = c.get(Calendar.MONTH) + 1;//获取当前月份
        int day = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        String monthStr;
        String dayStr;
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }

        mSelectMonth = Integer.valueOf(c.get(Calendar.YEAR) + monthStr);
        calendar.setDate(c.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr);


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
        getActivityDays();
        getActivities();
    }

    private void getActivityDays(){
        ActivityDaysReqEntity activityDaysReqEntity = new ActivityDaysReqEntity();
        activityDaysReqEntity.Month = mSelectMonth;
        RequestApi.getActivityDays(activityDaysReqEntity,new ResponseCallBack(mContext){
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if(resEntity == null) return;
                ActivityDaysResEntity activityDaysResEntity = (ActivityDaysResEntity) resEntity;
                calendar.setPoint(activityDaysResEntity.DayList);
            }
        } );
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

                if (mPageIndex == 1) {
                    mActivityAdapter.setNewData(mActivityResEntity.ActiveList);
                    //ptrClassicFrameLayout.refreshComplete();
                    mActivityAdapter.disableLoadMoreIfNotFullPage(rvCalendarActivity);
                } else {
                    mActivityAdapter.addData(mActivityResEntity.ActiveList);
                    mActivityAdapter.loadMoreComplete();
                }

                mIsEnd = mActivityResEntity.ActiveList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mActivityAdapter.getItemCount() == 0) {
                    ToastUtil.showShortToast("今天暂无活动");
                }
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
    }


    @Override
    public void onCalendarChanged(DateTime dateTime) {

        String monthStr = "";
        String dayStr = "";

        if (dateTime.getMonthOfYear() < 10) {
            monthStr = "0" + dateTime.getMonthOfYear();
        } else {
            monthStr = "" + dateTime.getMonthOfYear();
        }
        if ( dateTime.getDayOfMonth() < 10) {
            dayStr = "0" + dateTime.getDayOfMonth();
        } else {
            dayStr = "" + dateTime.getDayOfMonth();
        }

        mSelectMonth = Integer.valueOf("" + dateTime.getYear()  + monthStr);
        mSelectedDay = Integer.valueOf("" + dateTime.getYear() + monthStr + dayStr);
        tvCalendar.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月");
        getActivityDays();
        getActivities();

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
