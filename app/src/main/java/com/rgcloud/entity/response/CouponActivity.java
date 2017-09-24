package com.rgcloud.entity.response;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.activity.BaseActivity;
import com.rgcloud.adapter.CouponAdapter;
import com.rgcloud.adapter.PointAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.GlideUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;

import static com.rgcloud.R.style.dialog;

public class CouponActivity extends BaseActivity {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.rv_coupon)
    RecyclerView rvCoupon;

    private CouponAdapter mCouponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        initView();
        initData();
    }


    private void initView() {

        initTitleBar(R.id.tb_coupon, "我的卡券");

        rvCoupon.setLayoutManager(new LinearLayoutManager(mContext));
        rvCoupon.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).spaceResId(R.dimen.y10).showLastDivider().build());
        mCouponAdapter = new CouponAdapter(null);
        rvCoupon.setAdapter(mCouponAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getCoupon();
            }
        });

        rvCoupon.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CouponResEntity.CouponBean couponBean = (CouponResEntity.CouponBean) adapter.getItem(position);
                showCouponQR(couponBean.TicketUrl);
            }
        });
    }

    private void initData() {
        getCoupon();
    }

    private void showCouponQR(String qrImage) {


        View view = View.inflate(mContext, R.layout.dialog_coupon_qr, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.ConfirmDialogStyle)
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .show();


        ImageView ivQR = (ImageView) view.findViewById(R.id.iv_coupon_qr);
        GlideUtil.displayWithPlaceHolder(mContext, qrImage, R.mipmap.activity_temp, ivQR);

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
       /*  params.width = (int) getResources().getDimension(R.dimen.x500);
       params.height = (int) getResources().getDimension(R.dimen.x500);*/
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(params);

    }


    public void getCoupon() {
        RequestApi.getCoupon(new BaseReqEntity(), new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                CouponResEntity couponResEntity = (CouponResEntity) resEntity;
                mCouponAdapter.setNewData(couponResEntity.MemberTickets);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
            }
        });
    }

    @OnClick({R.id.btn_left_include_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left_include_title:
                finish();
                break;
        }
    }
}
