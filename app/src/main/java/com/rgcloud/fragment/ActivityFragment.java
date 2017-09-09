package com.rgcloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.activity.PostCommentActivity;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.NavigationAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.NavigationEntity;
import com.rgcloud.entity.response.ActivityResEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class ActivityFragment extends Fragment {

    @Bind(R.id.rv_activity_navigation)
    RecyclerView rvActivityNavigation;
    @Bind(R.id.rv_activity)
    RecyclerView rvActivity;

    private NavigationAdapter mNavigationAdapter;
    private ActivityAdapter mActivityAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        rvActivityNavigation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        List<NavigationEntity> navigationEntityList = new ArrayList<>();
        navigationEntityList.add(new NavigationEntity("全部", true));
        navigationEntityList.add(new NavigationEntity("演出", false));
        navigationEntityList.add(new NavigationEntity("展览", false));
        navigationEntityList.add(new NavigationEntity("讲座", false));
        navigationEntityList.add(new NavigationEntity("培训", false));
        mNavigationAdapter = new NavigationAdapter(navigationEntityList);
        rvActivityNavigation.setAdapter(mNavigationAdapter);

        rvActivityNavigation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NavigationEntity selectedNavigationEntity = mNavigationAdapter.getItem(position);
                for (int i = 0; i < mNavigationAdapter.getItemCount(); i++) {
                    NavigationEntity navigationEntity = mNavigationAdapter.getItem(i);
                    navigationEntity.hasSelected = navigationEntity.name.equals(selectedNavigationEntity.name);
                }
                mNavigationAdapter.notifyDataSetChanged();
            }
        });

        rvActivity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvActivity.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        mActivityAdapter = new ActivityAdapter(null);
        rvActivity.setAdapter(mActivityAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_search, R.id.rv_activity_navigation, R.id.rv_activity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), PostCommentActivity.class));
                break;
            case R.id.rv_activity_navigation:
                break;
            case R.id.rv_activity:
                break;
        }
    }
}
