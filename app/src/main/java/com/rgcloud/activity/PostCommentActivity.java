package com.rgcloud.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.rgcloud.entity.request.PostCommentReqEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.rgcloud.R;

public class PostCommentActivity extends BaseActivity {


    @Bind(R.id.et_post_comment)
    EditText etPostComment;

    private int mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mActivityId = getIntent().getIntExtra("activityId", 0);
    }

    private void initView() {
        initTitleBar(R.id.tb_post_comment, "评 论", "", "发表");
    }

    private void postComment() {
        if (TextUtils.isEmpty(etPostComment.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入评论内容");
            return;
        }
        PostCommentReqEntity postCommentReqEntity = new PostCommentReqEntity();
        postCommentReqEntity.ActiveId = mActivityId;
        postCommentReqEntity.Content = etPostComment.getText().toString().trim();
        RequestApi.postComment(postCommentReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ToastUtil.showShortToast("评论发表成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @OnClick({R.id.btn_right_include_title})
    public void onViewClicked(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_right_include_title:
                postComment();
                break;
        }
    }
}
