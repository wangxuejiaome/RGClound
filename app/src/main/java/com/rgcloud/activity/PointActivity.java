package com.rgcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.ActivitySpaceAdapter;
import com.rgcloud.adapter.PointAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.PointReqEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.entity.response.PointResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;

public class PointActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.tv_total_point)
    TextView tvTotalPoint;
    @Bind(R.id.rv_point)
    RecyclerView rvPoint;

    private PointAdapter mPointAdapter;
    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tvTotalPoint.setText("您有" + getIntent().getIntExtra("totalPoint", 0) + "积分");

        rvPoint.setLayoutManager(new LinearLayoutManager(mContext));
        rvPoint.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.y10).showLastDivider().build());
        mPointAdapter = new PointAdapter(null);
        rvPoint.setAdapter(mPointAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                getPoint();
            }
        });

        mPointAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvPoint.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsEnd) {
                            mPointAdapter.loadMoreEnd(true);
                            ToastUtil.showShortToast("没有更多了");
                        } else {
                            mPageIndex++;
                            getPoint();
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        getPoint();
    }

    public void getPoint() {
        RequestApi.getPointRecord(new PointReqEntity(mPageIndex, Constant.DEFAULT_PAGE_SIZE), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                PointResEntity pointResEntity = (PointResEntity) resEntity;
                if (mPageIndex == 1) {
                    mPointAdapter.setNewData(pointResEntity.RecordList);
                    ptrClassicFrameLayout.refreshComplete();
                    mPointAdapter.disableLoadMoreIfNotFullPage(rvPoint);
                } else {
                    mPointAdapter.addData(pointResEntity.RecordList);
                    mPointAdapter.loadMoreComplete();
                }

                mIsEnd = pointResEntity.RecordList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mPointAdapter.getItemCount() == 0) {
                    ToastUtil.showShortToast("暂无数据");
                }
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
                mPointAdapter.loadMoreFail();
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
