package com.rgcloud.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class GlideUtil {

    public static void displayNoPlaceHolde(Context context, String url,ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }

    public static void displayWithPlaceHolder(Context context, String url, int placeHolder, ImageView imageView){
        Glide.with(context).load(url).placeholder(placeHolder).into(imageView);
    }
}
