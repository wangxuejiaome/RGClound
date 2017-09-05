package com.rgcloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rgcloud.R;
import com.rgcloud.adapter.FunctionAdapter;
import com.rgcloud.adapter.FunctionNavigationAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.FunctionEntity;
import com.rgcloud.entity.response.HomeResEntity;
import com.stx.xhb.mylibrary.XBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class HomeFragment extends Fragment {

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

    private FunctionNavigationAdapter mFunctionNavigationAdapter;

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
        initView();
    }

    private void initView() {

        rvFunctionNavigation.setLayoutManager(new GridLayoutManager(getActivity(),4));
     /*   rvFunctionNavigation.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));*/
        List<HomeResEntity> homeResEntityList = new ArrayList<>();
        homeResEntityList.add(new HomeResEntity());
        homeResEntityList.add(new HomeResEntity());
        homeResEntityList.add(new HomeResEntity());
        homeResEntityList.add(new HomeResEntity());
        mFunctionNavigationAdapter = new FunctionNavigationAdapter(homeResEntityList);
        rvFunctionNavigation.setAdapter(mFunctionNavigationAdapter);

        rvFunction.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rvFunction.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x20).build());
        List<FunctionEntity> functionEntityList = new ArrayList<>();
        functionEntityList.add(new FunctionEntity("我爱直播","围观如皋精彩现场",R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("我要点单","百姓点单政府制单",R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("文化日历","每日活动新鲜速递",R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("文化地图","文化点位一栏无余",R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("民营剧团","特色好戏任你来点",R.mipmap.ic_live_function));
        functionEntityList.add(new FunctionEntity("志愿服务","志愿服务组织报名",R.mipmap.ic_live_function));
        FunctionAdapter functionAdapter = new FunctionAdapter(functionEntityList);
        rvFunction.setAdapter(functionAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                break;
        }
    }
}
