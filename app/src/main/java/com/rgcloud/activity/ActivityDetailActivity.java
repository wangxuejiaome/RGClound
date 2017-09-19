package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgcloud.R;
import com.rgcloud.entity.request.ActivityDetailReqEntity;
import com.rgcloud.entity.request.CollectCancelReqEntity;
import com.rgcloud.entity.request.CollectReqEntity;
import com.rgcloud.entity.request.GetTicketReqEntity;
import com.rgcloud.entity.response.ActivityDetailResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.GlideUtil;
import com.rgcloud.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDetailActivity extends BaseActivity {

    @Bind(R.id.iv_activity_detail)
    ImageView ivActivityDetail;
    @Bind(R.id.tv_name_activity_detail)
    TextView tvName;
    @Bind(R.id.ll_location)
    LinearLayout llLocation;
    @Bind(R.id.tv_address_activity_detail)
    TextView tvAddress;
    @Bind(R.id.tv_time_activity_detail)
    TextView tvTime;
    @Bind(R.id.tv_phone_activity_detail)
    TextView tvPhone;
    @Bind(R.id.wv_activity_detail)
    WebView wvActivityDetail;
    @Bind(R.id.tv_comment_activity_detail)
    TextView tvComment;
    @Bind(R.id.iv_collect_activity_detail)
    ImageView ivCollect;
    @Bind(R.id.iv_share_activity_detail)
    ImageView ivShare;
    @Bind(R.id.btn_get_ticket)
    Button btnGetTicket;

    private ActivityDetailResEntity mActivityDetailResEntity;

    private int mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mActivityId = getIntent().getIntExtra("activityId", 0);
        getActivityDetail();
    }

    private void setView() {
        GlideUtil.displayWithPlaceHolder(mContext, mActivityDetailResEntity.ActiveImage, R.mipmap.banner_default, ivActivityDetail);
        tvName.setText(mActivityDetailResEntity.ActiveName);
        tvAddress.setText(mActivityDetailResEntity.ActiveAddress);
        tvTime.setText(mActivityDetailResEntity.ActiveTime);
        tvPhone.setText(mActivityDetailResEntity.ConnectPhone);
        if (mActivityDetailResEntity.IsNeedTicket == 1) {
            btnGetTicket.setText("我要领票");
        } else {
            btnGetTicket.setText("免费参观");
        }

        //设置WebView属性，能够执行Javascript脚本
        wvActivityDetail.getSettings().setJavaScriptEnabled(true);
        wvActivityDetail.loadUrl(mActivityDetailResEntity.DetailUrl);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wvActivityDetail.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }

         /*   @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CirCleLoadingDialogUtil.showCircleProgressDialog(mContext);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }*/
        });

    }

    private void getActivityDetail() {
        ActivityDetailReqEntity activityDetailReqEntity = new ActivityDetailReqEntity();
        activityDetailReqEntity.ActiveId = mActivityId;
        RequestApi.getActivityDetail(activityDetailReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mActivityDetailResEntity = (ActivityDetailResEntity) resEntity;
                setView();
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
    }

    private void getTicket() {
        RequestApi.getTicket(new GetTicketReqEntity(mActivityId), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("获取门票成功");
                btnGetTicket.setText("已获门票");
            }
        });
    }

    private void collect() {
        RequestApi.collect(new CollectReqEntity(mActivityId), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("收藏成功");
                ivCollect.setImageResource(R.mipmap.ic_has_collect);
            }
        });
    }

    private void collectCancel() {
        RequestApi.collectCancel(new CollectCancelReqEntity(mActivityId), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("取消收藏成功");
                ivCollect.setImageResource(R.mipmap.ic_collect);
            }
        });
    }

    public static void startActivityDetail(Context context, int activityId) {
        Intent intent = new Intent(context, ActivityDetailActivity.class);
        intent.putExtra("activityId", activityId);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            wvActivityDetail.reload();
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_location, R.id.tv_phone_activity_detail, R.id.tv_comment_activity_detail, R.id.iv_collect_activity_detail, R.id.iv_share_activity_detail, R.id.btn_get_ticket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_location:
                break;
            case R.id.tv_phone_activity_detail:
                break;
            case R.id.tv_comment_activity_detail:
                Intent commentIntent = new Intent(mContext, PostCommentActivity.class);
                commentIntent.putExtra("activityId", mActivityId);
                startActivityForResult(commentIntent, 0);
                break;
            case R.id.iv_collect_activity_detail:
                collect();
                // collectCancel();
                break;
            case R.id.iv_share_activity_detail:
                break;
            case R.id.btn_get_ticket:
                if (btnGetTicket.getText().toString().equals("我要领票")) {
                    getTicket();
                }
                break;
        }
    }
}
