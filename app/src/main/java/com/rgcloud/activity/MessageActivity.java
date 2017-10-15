package com.rgcloud.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.InformationAdapter;
import com.rgcloud.adapter.MessageAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.request.MessageReqEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.MessageResEntity;
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

public class MessageActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_message)
    RecyclerView rvMessage;

    private MessageAdapter mMessageAdapter;
    private boolean mIsEnd;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_message, "消息中心");

        getMessage();

        rvMessage.setLayoutManager(new LinearLayoutManager(mContext));
        rvMessage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.x10).showLastDivider().build());
        mMessageAdapter = new MessageAdapter(null);
        rvMessage.setAdapter(mMessageAdapter);

        rvMessage.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageResEntity.MessageBean messageBean = (MessageResEntity.MessageBean) adapter.getItem(position);
                if (!TextUtils.isEmpty(messageBean.Params)) {

                    String[] flagAndId = messageBean.Params.split(",");
                    if (flagAndId[0].equals("0")) {
                        InformationDetailActivity.startActivityDetail(mContext, Integer.valueOf(flagAndId[1]));
                    } else {
                        ActivityDetailActivity.startActivityDetail(mContext, Integer.valueOf(flagAndId[1]));
                    }
                }
            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                getMessage();
            }
        });

        mMessageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsEnd) {
                            mMessageAdapter.loadMoreEnd(true);
                            ToastUtil.showShortToast("没有更多了");
                        } else {
                            mPageIndex++;
                            getMessage();
                        }
                    }
                });
            }
        });
    }

    private void getMessage() {

        RequestApi.getMessage(new MessageReqEntity(1, Constant.DEFAULT_PAGE_SIZE), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                MessageResEntity messageResEntity = (MessageResEntity) resEntity;
                if (mPageIndex == 1) {
                    mMessageAdapter.setNewData(messageResEntity.DataList);
                    ptrClassicFrameLayout.refreshComplete();
                    mMessageAdapter.disableLoadMoreIfNotFullPage(rvMessage);
                } else {
                    mMessageAdapter.addData(messageResEntity.DataList);
                    mMessageAdapter.loadMoreComplete();
                }

                mIsEnd = messageResEntity.DataList.size() < Constant.DEFAULT_PAGE_SIZE;

                if (mMessageAdapter.getItemCount() == 0) {
                    ToastUtil.showShortToast("暂无数据");
                }
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                ptrClassicFrameLayout.refreshComplete();
                mMessageAdapter.loadMoreFail();
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
