package com.rgcloud.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.CollectionAdapter;
import com.rgcloud.adapter.CommentAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.response.CollectionResEntity;
import com.rgcloud.entity.response.CommentResEntity;
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
import retrofit2.Call;

public class CommentActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_comment)
    RecyclerView rvComment;

    private CommentAdapter mCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {

        initTitleBar(R.id.tb_comment, "我的评论");

        rvComment.setLayoutManager(new LinearLayoutManager(mContext));
        rvComment.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.y10).showLastDivider().build());
        mCollectionAdapter = new CommentAdapter(null);
        rvComment.setAdapter(mCollectionAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getComment();
            }
        });

        rvComment.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*CommentResEntity.CommentBean commentBean = (CommentResEntity.CommentBean) adapter.getItem(position);
                ActivityDetailActivity.startActivityDetail(mContext, commentBean.);*/
            }
        });
    }

    private void initData() {
        getComment();
    }

    private void getComment() {
        RequestApi.getComment(new BaseReqEntity(), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                CommentResEntity commentResEntity = (CommentResEntity) resEntity;
                mCollectionAdapter.setNewData(commentResEntity.CommentList);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
                if(mCollectionAdapter.getItemCount() == 0){
                    ToastUtil.showShortToast("暂无数据");
                }
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
