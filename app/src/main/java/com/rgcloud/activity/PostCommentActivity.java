package com.rgcloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.rgcloud.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rgcloud.R;

public class PostCommentActivity extends BaseActivity {


    @Bind(R.id.et_post_comment)
    EditText etPostComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_post_comment, "评 论", "", "发表");
    }

    @OnClick({R.id.tb_post_comment, R.id.et_post_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_post_comment:
                break;
            case R.id.et_post_comment:
                break;
        }
    }
}
