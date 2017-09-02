package com.rgcloud.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rgcloud.R;


/**
 * Created by play on 2015/12/23.
 */
public class CircleLoadingProgressDialog extends Dialog {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ImageView ivLoading;

    public CircleLoadingProgressDialog(Context context) {
        this(context, R.style.common_dialog);
    }

    public CircleLoadingProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.circle_loading,null);
        ivLoading = (ImageView) view.findViewById(R.id.iv_loading);

        AnimationDrawable animationDrawable = (AnimationDrawable) ivLoading.getBackground();
        animationDrawable.start();
        setContentView(view,new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
    }

    protected CircleLoadingProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
