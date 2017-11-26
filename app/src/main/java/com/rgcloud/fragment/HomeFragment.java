package com.rgcloud.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.activity.ActivitiesActivity;
import com.rgcloud.activity.ActivityDetailActivity;
import com.rgcloud.activity.BindPhoneActivity;
import com.rgcloud.activity.CalendarActivity;
import com.rgcloud.activity.LiveActivity;
import com.rgcloud.activity.LoginActivity;
import com.rgcloud.activity.Main2Activity;
import com.rgcloud.activity.MapActivity;
import com.rgcloud.activity.OrderActivity;
import com.rgcloud.activity.SearchActivity;
import com.rgcloud.activity.WebviewActivity;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.FunctionAdapter;
import com.rgcloud.adapter.FunctionNavigationAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.response.ActivityResBean;
import com.rgcloud.entity.response.HomeResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.GlideUtil;
import com.rgcloud.util.PreferencesUtil;
import com.rgcloud.util.ToastUtil;
import com.stx.xhb.mylibrary.XBanner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class HomeFragment extends Fragment {

    @Bind(R.id.ptr_classic_frame_layout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.banner_home)
    XBanner bannerHome;
    @Bind(R.id.rv_function_navigation)
    RecyclerView rvFunctionNavigation;
    @Bind(R.id.rv_function)
    RecyclerView rvFunction;
    @Bind(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @Bind(R.id.tv_live)
    TextView tvLive;
    @Bind(R.id.ll_live)
    RelativeLayout llLive;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.ll_service)
    LinearLayout llService;

    private PreferencesUtil mPreferencesUtil;

    private FunctionNavigationAdapter mFunctionNavigationAdapter;
    private ActivityAdapter mActivityAdapter;
    private HomeResEntity mHomeResEntity;
    private FunctionAdapter mFunctionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPreferencesUtil = new PreferencesUtil(getActivity());
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerHome.startAutoPlay();
    }


    private void initView() {

        rvFunctionNavigation.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        List<FunctionEntity> functionEntityList = new ArrayList<>();
        functionEntityList.add(new FunctionEntity("重大活动", "", R.mipmap.ic_speaker_function));
        functionEntityList.add(new FunctionEntity("精彩回放", "", R.mipmap.ic_tv_function));
        functionEntityList.add(new FunctionEntity("文化日历", "", R.mipmap.ic_calendar_function));
        functionEntityList.add(new FunctionEntity("我要点单", "", R.mipmap.ic_order_function));

        mFunctionNavigationAdapter = new FunctionNavigationAdapter(functionEntityList);
        rvFunctionNavigation.setAdapter(mFunctionNavigationAdapter);

        rvFunctionNavigation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (position) {
                    case 0://重大活动
                        if (mHomeResEntity != null) {
                            ActivitiesActivity.startActivitiesActivity(getActivity(), 1, mHomeResEntity.MainImportantActiveId, 0);
                        }
                        break;
                    case 1://精彩回放
                        if (mHomeResEntity != null) {
                            ActivitiesActivity.startActivitiesActivity(getActivity(), 1, -1, 0);
                        }
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),CalendarActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                        break;
                }

              /*  HomeResEntity.IconListBean iconListBean = (HomeResEntity.IconListBean) adapter.getItem(position);
                ActivitiesActivity.startActivitiesActivity(getActivity(), 1, iconListBean.TypeId, 0);*/
            }
        });


    /*    rvFunction.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvFunction.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x20).build());
        List<FunctionEntity> functionEntityList = new ArrayList<>();
        functionEntityList.add(new FunctionEntity("我爱直播", "围观如皋精彩现场", R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("我要点单", "百姓点单政府制单", R.mipmap.ic_order_function));
        functionEntityList.add(new FunctionEntity("文化日历", "每日活动新鲜速递", R.mipmap.ic_calendar_function));
        functionEntityList.add(new FunctionEntity("文化地图", "文化点位一栏无余", R.mipmap.ic_map_function));
        functionEntityList.add(new FunctionEntity("民营剧团", "特色好戏任你来点", R.mipmap.ic_troupe_function));
        functionEntityList.add(new FunctionEntity("志愿服务", "志愿服务组织报名", R.mipmap.ic_service_function));
        mFunctionAdapter = new FunctionAdapter(functionEntityList);
        rvFunction.setAdapter(mFunctionAdapter);

        rvFunction.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                FunctionEntity functionEntity = mFunctionAdapter.getItem(position);
                switch (functionEntity.name) {
                    case "我爱直播":


                        break;
                    case "我要点单":

                        break;
                    case "文化日历":

                        break;
                    case "文化地图":

                        break;
                    case "民营剧团":
                        break;
                    case "志愿服务":
                        break;
                }
            }
        });*/

        rvRecommend.setNestedScrollingEnabled(false);
        rvRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecommend.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivityAdapter = new ActivityAdapter(null);
        rvRecommend.setAdapter(mActivityAdapter);

        rvRecommend.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityResBean activityResBean = mActivityAdapter.getItem(position);
                ActivityDetailActivity.startActivityDetail(getActivity(), activityResBean.ActiveId);
            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getHomeInfo();
            }
        });
    }

    private void initData() {
        getHomeInfo();
    }

    private void setView() {

        if (mHomeResEntity.TopChangeImageUrl != null) {
            bannerHome.setData(mHomeResEntity.TopChangeImageUrl, null);
            bannerHome.setmAdapter(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageView imageView = (ImageView) view;
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    GlideUtil.displayWithPlaceHolder(getActivity(), (String) model, R.mipmap.banner_default, imageView);
                }
            });
        }

        //   mFunctionNavigationAdapter.setNewData(mHomeResEntity.IconList);
        mActivityAdapter.setNewData(mHomeResEntity.RecommendList);
    }

    private void getHomeInfo() {
        RequestApi.getHomeInfo(new BaseReqEntity(), new ResponseCallBack(getActivity()) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                mHomeResEntity = (HomeResEntity) resEntity;
                setView();
                ((Main2Activity) getActivity()).setSearchKey(mHomeResEntity.HotSearchKeyWords);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
                ptrClassicFrameLayout.refreshComplete();
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        bannerHome.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_search, R.id.ll_live, R.id.ll_culture_red_packet, R.id.ll_map, R.id.ll_art, R.id.ll_culture, R.id.ll_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (mHomeResEntity != null) {
                    Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                    searchIntent.putExtra("hotSearchKeys", (Serializable) mHomeResEntity.HotSearchKeyWords);
                    startActivity(searchIntent);
                }
                break;
            case R.id.ll_live:
                if (!mPreferencesUtil.getBoolean(PreferencesUtil.HAS_LOGIN)) {
                    ToastUtil.showShortToast("请先登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }

                if (TextUtils.isEmpty(mPreferencesUtil.getString(PreferencesUtil.USER_PHONE))) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1
                            .setMessage("您好，微信登录的用户需要在设置中先绑定手机号再看直播")
                            .setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getActivity(), BindPhoneActivity.class));
                                }
                            }).setNegativeButton("取消", null)
                            .show();
                    return;
                } else {
                    startActivity(new Intent(getActivity(), LiveActivity.class));
                }
                break;
            case R.id.ll_culture_red_packet:
                if (mHomeResEntity == null) return;
                if (TextUtils.isEmpty(mHomeResEntity.CulturalWalletUrl)) {
                    ToastUtil.showShortToast("暂未开放");
                } else {

                    if (!mPreferencesUtil.getBoolean(PreferencesUtil.HAS_LOGIN)) {
                        ToastUtil.showShortToast("请先登录");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    } else {
                        WebviewActivity.startWebView(getActivity(), "文化红包", mHomeResEntity.CulturalWalletUrl + "?Token=" + mPreferencesUtil.getString(PreferencesUtil.ACCESS_TOKEN));
                    }
                }
                break;

            case R.id.ll_map:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;

            case R.id.ll_art:
                if (mHomeResEntity != null) {
                    ActivitiesActivity.startActivitiesActivity(getActivity(), 1,mHomeResEntity.ArtisticAppreciation, 0);
                }
                break;
            case R.id.ll_culture:
                if (mHomeResEntity != null) {
                    ActivitiesActivity.startActivitiesActivity(getActivity(), 1,mHomeResEntity.CulturalTreasures, 0);
                }
                break;
            case R.id.ll_service:
                if (mHomeResEntity != null) {
                    ActivitiesActivity.startActivitiesActivity(getActivity(), 1, mHomeResEntity.VoluntaryServiceId, 0);
                }
                break;
        }
    }
}
