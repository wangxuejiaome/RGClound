package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.entity.request.ActivityDetailReqEntity;
import com.rgcloud.entity.request.CollectReqEntity;
import com.rgcloud.entity.request.GetTicketReqEntity;
import com.rgcloud.entity.response.ActivityDetailResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.GlideUtil;
import com.rgcloud.util.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationDetailActivity extends BaseActivity {

    @Bind(R.id.iv_activity_detail)
    ImageView ivActivityDetail;
    @Bind(R.id.wv_information_detail)
    WebView wvInformationDetail;
    @Bind(R.id.btn_get_ticket)
    Button btnGetTicket;

    private SHARE_MEDIA mShareMedia = SHARE_MEDIA.WEIXIN;
    private ActivityDetailResEntity mActivityDetailResEntity;
    private android.app.AlertDialog mShareDialog;
    private int mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        mActivityId = getIntent().getIntExtra("activityId", 0);
        getActivityDetail();
    }

    private void setView() {
        GlideUtil.displayWithPlaceHolder(mContext, mActivityDetailResEntity.ActiveImage, R.mipmap.banner_default, ivActivityDetail);
        if (mActivityDetailResEntity.IsNeedTicket == 1) {
            btnGetTicket.setText("我要抢票");
        } else {
            btnGetTicket.setText("免费参观");
        }

        //设置WebView属性，能够执行Javascript脚本
        wvInformationDetail.getSettings().setJavaScriptEnabled(true);
        wvInformationDetail.loadUrl(mActivityDetailResEntity.DetailUrl);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wvInformationDetail.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
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
                ToastUtil.showShortToast("抢票成功");
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
                //ivCollect.setImageResource(R.mipmap.ic_has_collect);
            }
        });
    }

    /**
     * 展示分享界面
     */
    private void showShareDialog() {

        View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
        mShareDialog = new android.app.AlertDialog.Builder(this, R.style.ConfirmDialogStyle).create();
        mShareDialog.show();// 显示创建的AlertDialog，并显示，必须放在Window设置属性之前

        Window window = mShareDialog.getWindow();
        if (window != null) {
            window.setContentView(view);//这一步必须指定，否则不出现弹窗
            WindowManager.LayoutParams mParams = window.getAttributes();
            mParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setBackgroundDrawableResource(android.R.color.white);
            window.setAttributes(mParams);
        }

        Button btn_wx = (Button) view.findViewById(R.id.btn_share_wx);
        Button btn_circle = (Button) view.findViewById(R.id.btn_share_circle);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_share_cancel);

        btn_wx.setOnClickListener(mShareBtnClickListen);
        btn_circle.setOnClickListener(mShareBtnClickListen);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareDialog.dismiss();
            }
        });
    }

    private View.OnClickListener mShareBtnClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String shareTitle = "如皋文化云";

            switch (view.getId()) {
                case R.id.btn_share_wx:
                    mShareMedia = SHARE_MEDIA.WEIXIN;
                    break;
                case R.id.btn_share_circle:
                    mShareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                    shareTitle = mActivityDetailResEntity.ActiveName;
                    break;
                default:
                    break;
            }

            ShareAction shareAction = new ShareAction(InformationDetailActivity.this);
            UMImage image = new UMImage(InformationDetailActivity.this, R.mipmap.ic_launcher);//资源文件
            UMWeb web = new UMWeb(mActivityDetailResEntity.SharedUrl);
            web.setThumb(image);
            web.setTitle(shareTitle);
            web.setDescription(mActivityDetailResEntity.ActiveAddress);
            shareAction.withMedia(web);
            //  shareAction.withText(mActivityDetailResEntity.ActiveName + "景点正在开放，欢迎前来观赏");


            shareAction.setCallback(shareListener);
            shareAction.setPlatform(mShareMedia).share();
        }
    };

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InformationDetailActivity.this, "成功了", Toast.LENGTH_LONG).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }

        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InformationDetailActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InformationDetailActivity.this, "取消了", Toast.LENGTH_LONG).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }

        }
    };

    public static void startActivityDetail(Context context, int activityId) {
        Intent intent = new Intent(context, InformationDetailActivity.class);
        intent.putExtra("activityId", activityId);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            wvInformationDetail.reload();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_comment_information_detail, R.id.iv_collect_information_detail, R.id.iv_share_information_detail, R.id.btn_get_ticket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_comment_information_detail:
                Intent commentIntent = new Intent(mContext, PostCommentActivity.class);
                commentIntent.putExtra("activityId", mActivityId);
                startActivityForResult(commentIntent, 0);
                break;
            case R.id.iv_collect_information_detail:
                collect();
                break;
            case R.id.iv_share_information_detail:
                showShareDialog();
                break;
            case R.id.btn_get_ticket:
                if (btnGetTicket.getText().toString().equals("我要抢票")) {
                    getTicket();
                }
                break;
        }
    }
}
