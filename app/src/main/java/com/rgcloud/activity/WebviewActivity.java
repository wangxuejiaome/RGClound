package com.rgcloud.activity;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.http.ServiceGenerator;
import com.rgcloud.util.CirCleLoadingDialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebviewActivity extends BaseActivity {


    @Bind(R.id.webview)
    WebView webview;
    private String url;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        initView();
        setView();
    }

    private void initView() {

        name = getIntent().getStringExtra("name");
        initTitleBar(R.id.tb_webview, name);

        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        url = getIntent().getStringExtra("url");
        setView();
    }

    private void setView() {

        webview.loadUrl(url);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CirCleLoadingDialogUtil.showCircleProgressDialog(mContext, "请稍后……");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });

    }

    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void startWebView(Context context, String name, String url) {

        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @OnClick({R.id.btn_left_include_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_include_title:
                finish();
                break;
        }
    }
}
