package com.rgcloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgcloud.R;
import com.rgcloud.activity.CollectionActivity;
import com.rgcloud.activity.CommentActivity;
import com.rgcloud.activity.LiveActivity;
import com.rgcloud.activity.MessageActivity;
import com.rgcloud.activity.PointActivity;
import com.rgcloud.activity.SettingActivity;
import com.rgcloud.activity.WebviewActivity;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.response.CouponActivity;
import com.rgcloud.entity.response.PersonalInfoResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.PreferencesUtil;
import com.rgcloud.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class PersonalFragment extends Fragment {

    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_coupon_personal)
    TextView tvCoupon;
    @Bind(R.id.tv_collection_personal)
    TextView tvCollection;
    @Bind(R.id.tv_comment_personal)
    TextView tvComment;
    @Bind(R.id.ll_live_personal)
    LinearLayout llLive;
    @Bind(R.id.ll_point_personal)
    LinearLayout llPoint;
    @Bind(R.id.tv_available_point)
    TextView tvAvailablePoint;
    @Bind(R.id.ll_setting)
    LinearLayout llSetting;
    @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;
    private PersonalInfoResEntity mPersonalInfoResEntity;

    PreferencesUtil mPreferencesUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferencesUtil = new PreferencesUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPersonalInfo();
    }

    private void getPersonalInfo() {
        RequestApi.getPersonalInfo(new BaseReqEntity(), new ResponseCallBack(getActivity()) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                mPersonalInfoResEntity = (PersonalInfoResEntity) resEntity;
                if (mPersonalInfoResEntity.CanDirectSeeding == 1) {
                    llLive.setVisibility(View.VISIBLE);
                }
                tvNickName.setText(mPersonalInfoResEntity.MemberNickName);
                tvAvailablePoint.setText(mPersonalInfoResEntity.RemindPoint + "积分");
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_message, R.id.iv_header, R.id.tv_coupon_personal, R.id.tv_collection_personal, R.id.tv_comment_personal, R.id.ll_live_personal, R.id.ll_point_personal, R.id.ll_culture_red_packet, R.id.ll_volunteen_personal, R.id.ll_setting, R.id.ll_about_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.iv_header:
                break;
            case R.id.tv_coupon_personal:
                startActivity(new Intent(getActivity(), CouponActivity.class));
                break;
            case R.id.tv_collection_personal:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            case R.id.tv_comment_personal:
                startActivity(new Intent(getActivity(), CommentActivity.class));
                break;
            case R.id.ll_live_personal:
                Intent liveIntent = new Intent(getActivity(), LiveActivity.class);
                liveIntent.putExtra("isHost", true);
                startActivity(liveIntent);
                break;
            case R.id.ll_point_personal:
                Intent pointIntent = new Intent(getActivity(), PointActivity.class);
                if (mPersonalInfoResEntity != null) {
                    pointIntent.putExtra("totalPoint", mPersonalInfoResEntity.TotalPoint);
                }
                startActivity(pointIntent);
                break;
            case R.id.ll_culture_red_packet:
                if(mPersonalInfoResEntity == null) return;
                if(TextUtils.isEmpty(mPersonalInfoResEntity.MyCultureWalletUrl)){
                    ToastUtil.showShortToast("暂未开放");
                }else {
                    WebviewActivity.startWebView(getActivity(), "文化红包", mPersonalInfoResEntity.MyCultureWalletUrl   + "?Token=" + mPreferencesUtil.getString(PreferencesUtil.ACCESS_TOKEN));
                }
                break;
            case R.id.ll_volunteen_personal:
                if(mPersonalInfoResEntity == null) return;
                if(TextUtils.isEmpty(mPersonalInfoResEntity.MyVolunteerUrl)){
                    ToastUtil.showShortToast("暂未开放");
                }else {
                    WebviewActivity.startWebView(getActivity(), "我的志愿", mPersonalInfoResEntity.MyVolunteerUrl  + "?Token=" + mPreferencesUtil.getString(PreferencesUtil.ACCESS_TOKEN));
                }
                break;
            case R.id.ll_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_about_us:
                if (mPersonalInfoResEntity == null) return;
                WebviewActivity.startWebView(getActivity(), "关于我们", mPersonalInfoResEntity.AboutUsUrl);
                break;
        }
    }
}
