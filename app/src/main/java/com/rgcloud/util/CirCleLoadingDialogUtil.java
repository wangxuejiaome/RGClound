package com.rgcloud.util;

import android.app.Activity;
import android.content.Context;

import com.rgcloud.view.CircleLoadingProgressDialog;


/**
 * Created by play on 2016/3/24.
 */
public class CirCleLoadingDialogUtil {

    private  static CircleLoadingProgressDialog mCircleLoadingProgressDialog;

    /**
     * 显示默认的圆形加载进度框,触摸焦点不消失，返回不可消失
     */
    public static void showCircleProgressDialog(Context context) {

        Activity activity = (Activity) context;

        if(!activity.isFinishing()){

            while (activity.getParent() != null) {
                activity = activity.getParent();
            }
            if(mCircleLoadingProgressDialog == null){
                mCircleLoadingProgressDialog = new CircleLoadingProgressDialog(context);
                mCircleLoadingProgressDialog.setCanceledOnTouchOutside(false);
            }

            mCircleLoadingProgressDialog.show();
        }
    }

    /**
     * 显示圆形加载进度框，设置提示返回不可以消失
     *
     * @param context
     * @param message
     */
    public static void showCircleProgressDialogNotCancel(Context context, String message) {

        Activity activity = (Activity) context;

        if(!activity.isFinishing()){

            while (activity.getParent() != null) {
                activity = activity.getParent();
            }
            if(mCircleLoadingProgressDialog == null){
                mCircleLoadingProgressDialog = new CircleLoadingProgressDialog(context);
                mCircleLoadingProgressDialog.setCancelable(false);
            }

            mCircleLoadingProgressDialog.show();
        }
    }


    /**
     * 让进度框消失
     */
    public static void dismissCircleProgressDialog() {

        if (null != mCircleLoadingProgressDialog) {
            mCircleLoadingProgressDialog.dismiss();
            mCircleLoadingProgressDialog = null;
        }
    }
}
