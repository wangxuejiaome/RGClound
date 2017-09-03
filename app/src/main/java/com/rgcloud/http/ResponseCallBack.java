package com.rgcloud.http;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.rgcloud.R;
import com.rgcloud.activity.LoginActivity;
import com.rgcloud.application.AppActivityManager;
import com.rgcloud.entity.BaseResEntity;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.util.Util;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by play on 2016/6/21.
 */
public class ResponseCallBack<T> implements Callback<T> {

    private static final String TAG = "ResponseCallBack";

    public Context mContext;

    public ResponseCallBack(Context context) {
        mContext = context;
    }

    public void onObjectResponse(T resEntity) {
    }

    private void onFailure(Throwable t) {
        CirCleLoadingDialogUtil.dismissCircleProgressDialog();
        if (mContext == null) return;

        if (!Util.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, "请检查您的网络是否连接", Toast.LENGTH_SHORT).show();
        } else if (t.getClass().equals(SocketTimeoutException.class)) {
            Toast.makeText(mContext, "网络访问超时，请稍后再试", Toast.LENGTH_SHORT).show();
        } else if (mContext != null) {
            Toast.makeText(mContext, "发生未知错误，请稍后再试", Toast.LENGTH_SHORT).show();
        }
        t.printStackTrace();
        Log.i("failure", "----->Throwable:" + t.toString());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            BaseResEntity resEntity = (BaseResEntity) response.body();
            if (resEntity.Code.equals("200")) {
                onObjectResponse(response.body());
            } else {
                if (mContext != null) {
                    CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                }
                if (!TextUtils.isEmpty(resEntity.Message)) {
                    ToastUtil.showShortToast(resEntity.Message);
                }
                switch (resEntity.Code) {
                    case "301"://登陆后再试
                    case "303"://需要重新登录
                        Intent loginAgainIntent = new Intent(mContext, LoginActivity.class);
                        loginAgainIntent.putExtra("loginType", 2);
                        mContext.startActivity(loginAgainIntent);
                        break;
                    case "408"://账户已在别处登陆，是否强制登陆
                        mLoginInterface.loginAnyway();
                        /*Intent loginAnywayIntent = new Intent(mContext, LoginActivity.class);
                        loginAnywayIntent.putExtra("loginType",1);
                        mContext.startActivity(loginAnywayIntent);*/
                        break;
                    default:
                        ToastUtil.showShortToast("服务器异常，请稍候再试");
                        break;
                }
            }
        } else {
            ToastUtil.showShortToast("访问异常code:" + response.code());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t);
    }

    private static LoginInterface mLoginInterface;

    public static void setLoginInterface(LoginInterface loginInterface) {
        mLoginInterface = loginInterface;
    }

    public interface LoginInterface {
        void loginAnyway();
    }
}