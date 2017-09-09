package com.rgcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rgcloud.R;
import com.rgcloud.entity.request.OrderReqEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @Bind(R.id.tb_order)
    TitleBar tbOrder;
    @Bind(R.id.et_title_order)
    EditText etTitle;
    @Bind(R.id.rg_order)
    RadioGroup rgOrder;
    @Bind(R.id.rb_consult)
    RadioButton rbConsult;
    @Bind(R.id.rb_advices)
    RadioButton rbAdvices;
    @Bind(R.id.rb_order)
    RadioButton rbOrder;
    @Bind(R.id.et_content_order)
    EditText etContent;
    @Bind(R.id.tv_input_count_order)
    TextView tvInputCount;
    @Bind(R.id.btn_order)
    Button btnOrder;

    private Integer mOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTitleBar(R.id.tb_order, "我要点单");

        rgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_consult:
                        mOrderType = 1;
                        break;
                    case R.id.rb_advices:
                        mOrderType = 2;
                        break;
                    case R.id.rb_order:
                        mOrderType = 3;
                        break;
                }
            }
        });

        /**
         * 监听用户输入的字符个数
         */
        etContent.addTextChangedListener(new TextWatcher() {

            int maxCount = 120;
            int selectionStart;
            int selectionEnd;
            int inputNumber;
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                inputNumber = s.length();
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

                tvInputCount.setText(inputNumber + "/120");
                selectionStart = etContent.getSelectionStart();
                selectionEnd = etContent.getSelectionEnd();

                tvInputCount.setText(inputNumber + "/200");
                if (temp.length() > maxCount) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etContent.setText(s);
                    etContent.setSelection(tempSelection);//设置光标在最后
                    Toast.makeText(OrderActivity.this, "评论字数不可以超过200！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入标题");
            return false;
        }
        if (mOrderType == null) {
            ToastUtil.showShortToast("请选择类别");
            return false;
        }
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            ToastUtil.showShortToast("请输入内容");
            return false;
        }
        return true;
    }

    private void order() {
        if(!checkValidate()) return;
        OrderReqEntity orderReqEntity = new OrderReqEntity();
        orderReqEntity.Title = etTitle.getText().toString().trim();
        orderReqEntity.Type = mOrderType;
        orderReqEntity.Content = etContent.getText().toString().trim();
        RequestApi.order(orderReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                ToastUtil.showShortToast("提交成功");
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                finish();
            }
        });
    }

    @OnClick({R.id.btn_order})
    public void onViewClicked(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_order:
                order();
                break;
        }
    }
}
