package com.rgcloud.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.rgcloud.R;
import com.rgcloud.config.Constant;
import com.rgcloud.entity.request.ActivityDetailReqEntity;
import com.rgcloud.entity.request.CollectCancelReqEntity;
import com.rgcloud.entity.request.CollectReqEntity;
import com.rgcloud.entity.request.GetTicketReqEntity;
import com.rgcloud.entity.response.ActivityDetailResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.BNEventHandler;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.GlideUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.util.Util;
import com.rgcloud.view.CircleLoadingProgressDialog;
import com.tencent.qalsdk.im_open.http;
import com.tencent.qcloud.xiaozhibo.play.TCLivePlayerActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rgcloud.activity.MapActivity.ROUTE_PLAN_NODE;
import static java.util.ResourceBundle.clearCache;

public class ActivityDetailActivity extends BaseActivity {

    @Bind(R.id.activity_detail)
    LinearLayout llActivityDetail;
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
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;

    private SHARE_MEDIA mShareMedia = SHARE_MEDIA.WEIXIN;

    private final static String authBaseArr[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    private BNRoutePlanNode.CoordinateType mCoordinateType = BNRoutePlanNode.CoordinateType.BD09LL;
    private String mSDCardPath = null;
    String authinfo = null;
    public static List<Activity> activityList = new LinkedList<Activity>();

    private ActivityDetailResEntity mActivityDetailResEntity;
    private int mActivityId;
    private double mEndLng;
    private double mEndLat;
    private android.app.AlertDialog mShareDialog;
    private InsideWebChromeClient mInsideWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        BNOuterLogUtil.setLogSwitcher(true);
        initWebView();
        initData();

    }

    private void initWebView() {
        WebSettings settings = wvActivityDetail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        //settings.setPluginsEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mInsideWebChromeClient = new InsideWebChromeClient();
        InsideWebViewClient mInsideWebViewClient = new InsideWebViewClient();
        //javascriptInterface = new JavascriptInterface();
        //mWebView.addJavascriptInterface(javascriptInterface, "java2js_laole918");
        wvActivityDetail.setWebChromeClient(mInsideWebChromeClient);
        wvActivityDetail.setWebViewClient(mInsideWebViewClient);

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
            if(mActivityDetailResEntity.RemaindTicket == 0){
                btnGetTicket.setEnabled(false);
                btnGetTicket.setBackgroundColor(0xffcccccc);
            }else {
                btnGetTicket.setEnabled(true);
                btnGetTicket.setBackgroundColor(0xff2765b3);
            }
            btnGetTicket.setText("我要领票(" + mActivityDetailResEntity.RemaindTicket + ")");
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
                mEndLng = mActivityDetailResEntity.Latitude;
                mEndLat = mActivityDetailResEntity.Longitude;
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
                //ivCollect.setImageResource(R.mipmap.ic_has_collect);
            }
        });
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, Constant.BD_NAVIGATION_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean hasBasePhoneAuth() {

        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {

        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {

        // 申请权限
        if (Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, Constant.BD_NAVIGATION_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                ActivityDetailActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(ActivityDetailActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(ActivityDetailActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();

                routeplanToNavi(mEndLng, mEndLat);
            }

            public void initStart() {
                Toast.makeText(ActivityDetailActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(ActivityDetailActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }

        }, null, null, null);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(double endLng, double endLat) {
        if (!hasInitSuccess) {
            Toast.makeText(ActivityDetailActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(ActivityDetailActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(118.805047, 31.974972, "", null, mCoordinateType);
        eNode = new BNRoutePlanNode(mEndLng, mEndLat, "", null, mCoordinateType);

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
        // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new ActivityDetailActivity.DemoRoutePlanListener(sNode), eventListerner);
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(ActivityDetailActivity.this, BNGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(ActivityDetailActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10150203");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(ActivityDetailActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            routeplanToNavi(mEndLng, mEndLat);
        }

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

            String  shareTitle = "如皋文化云";

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

            ShareAction shareAction = new ShareAction(ActivityDetailActivity.this);
            UMImage image = new UMImage(ActivityDetailActivity.this, R.mipmap.ic_launcher);//资源文件
            UMWeb web = new UMWeb(mActivityDetailResEntity.SharedUrl);
            web.setThumb(image);
            web.setTitle(shareTitle);
            web.setDescription(mActivityDetailResEntity.ActiveName);
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
            Toast.makeText(ActivityDetailActivity.this, "成功了", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ActivityDetailActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(ActivityDetailActivity.this, "取消了", Toast.LENGTH_LONG).show();
            if (mShareDialog != null) {
                mShareDialog.dismiss();
            }

        }
    };

    private class InsideWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            llActivityDetail.addView(mCustomView);
            mCustomViewCallback = callback;
            wvActivityDetail.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            wvActivityDetail.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            llActivityDetail.removeView(mCustomView);
            llBottom.setVisibility(View.VISIBLE);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }
    }


    private class InsideWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //mWebView.loadUrl(javascript);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        wvActivityDetail.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wvActivityDetail.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wvActivityDetail.canGoBack()) {
            wvActivityDetail.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        wvActivityDetail.destroy();
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.ll_location, R.id.tv_phone_activity_detail, R.id.tv_comment_activity_detail, R.id.iv_collect_activity_detail, R.id.iv_share_activity_detail, R.id.btn_get_ticket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_location:
                if (mActivityDetailResEntity == null) return;
                CirCleLoadingDialogUtil.showCircleProgressDialog(mContext, "导航准备中，请稍等……");
                if (initDirs()) {
                    initNavi();
                }
                break;
            case R.id.tv_phone_activity_detail:
                if (mActivityDetailResEntity == null) return;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("您确定要拨打电话" + mActivityDetailResEntity.ConnectPhone + "吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Util.callPhone(ActivityDetailActivity.this, mActivityDetailResEntity.ConnectPhone);
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.tv_comment_activity_detail:
                Intent commentIntent = new Intent(mContext, PostCommentActivity.class);
                commentIntent.putExtra("activityId", mActivityId);
                startActivityForResult(commentIntent, 0);
                break;
            case R.id.iv_collect_activity_detail:
                collect();
                break;
            case R.id.iv_share_activity_detail:
                showShareDialog();
                break;
            case R.id.btn_get_ticket:
                if (btnGetTicket.getText().toString().equals("我要领票")) {
                    getTicket();
                }
                break;
        }
    }
}
