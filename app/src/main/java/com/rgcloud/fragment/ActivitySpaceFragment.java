package com.rgcloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rgcloud.R;
import com.rgcloud.adapter.ActivityAdapter;
import com.rgcloud.adapter.ActivitySpaceAdapter;
import com.rgcloud.divider.HorizontalDividerItemDecoration;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class ActivitySpaceFragment extends Fragment {

    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.rv_activity_space)
    RecyclerView rvActivitySpace;

    private ActivitySpaceAdapter mActivitySpaceAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_space, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        rvActivitySpace.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvActivitySpace.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).spaceResId(R.dimen.x10).showLastDivider().build());
        List<ActivitySpaceResEntity> activityResEntityList = new ArrayList<>();
        activityResEntityList.add(new ActivitySpaceResEntity());
        activityResEntityList.add(new ActivitySpaceResEntity());
        activityResEntityList.add(new ActivitySpaceResEntity());
        mActivitySpaceAdapter = new ActivitySpaceAdapter(activityResEntityList);
        rvActivitySpace.setAdapter(mActivitySpaceAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
    }
}
