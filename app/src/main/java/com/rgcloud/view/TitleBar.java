package com.rgcloud.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rgcloud.R;


/**
 * Created by play on 2016/3/24.
 */
public class TitleBar extends RelativeLayout {

    public Button btnLeft;
    public ImageButton imgBtnLeft;
    public TextView tvTitle;
    public Button btnRight;
    public ImageButton imgBtnRight;


    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        btnLeft = (Button) this.findViewById(R.id.btn_left_include_title);
        tvTitle = (TextView) this.findViewById(R.id.tv_content_include_title);
        btnRight = (Button) this.findViewById(R.id.btn_right_include_title);
        imgBtnRight = (ImageButton) this.findViewById(R.id.img_btn_right_include_title);
    }
}
