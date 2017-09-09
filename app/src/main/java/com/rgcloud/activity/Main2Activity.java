package com.rgcloud.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rgcloud.R;
import com.rgcloud.fragment.ActivityFragment;
import com.rgcloud.fragment.ActivitySpaceFragment;
import com.rgcloud.fragment.HomeFragment;
import com.rgcloud.fragment.InformationFragment;
import com.rgcloud.fragment.PersonalFragment;
import com.rgcloud.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends BaseActivity {

    @Bind(R.id.fl_content_main)
    FrameLayout flContentMain;
    @Bind(R.id.tv_home_tab)
    TextView tvHomeTab;
    @Bind(R.id.tv_information_tab)
    TextView tvInformationTab;
    @Bind(R.id.tv_activity_tab)
    TextView tvActivityTab;
    @Bind(R.id.tv_activity_space_tab)
    TextView tvActivitySpaceTab;
    @Bind(R.id.tv_personal_tab)
    TextView tvPersonalTab;

    private HomeFragment mHomeFragment;
    private InformationFragment mInformationFragment;
    private ActivityFragment mActivityFragment;
    private ActivitySpaceFragment mActivitySpaceFragment;
    private PersonalFragment mPersonalFragment;

    private List<String> mSearchKey;

    public List<String> getSearchKey() {
        return mSearchKey;
    }

    public void setSearchKey(List<String> searchKey) {
        mSearchKey = searchKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        tvHomeTab.performClick();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    private void setFragmentShow(int selectTabIndex) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (mHomeFragment != null) transaction.hide(mHomeFragment);
        if (mInformationFragment != null) transaction.hide(mInformationFragment);
        if (mActivityFragment != null) transaction.hide(mActivityFragment);
        if (mActivitySpaceFragment != null) transaction.hide(mActivitySpaceFragment);
        if (mPersonalFragment != null) transaction.hide(mPersonalFragment);

        switch (selectTabIndex) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content_main, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                if (mInformationFragment == null) {
                    mInformationFragment = new InformationFragment();
                    transaction.add(R.id.fl_content_main, mInformationFragment);
                } else {
                    transaction.show(mInformationFragment);
                }
                break;
            case 2:
                if (mActivityFragment == null) {
                    mActivityFragment = new ActivityFragment();
                    transaction.add(R.id.fl_content_main, mActivityFragment);
                } else {
                    transaction.show(mActivityFragment);
                }
                break;
            case 3:
                if (mActivitySpaceFragment == null) {
                    mActivitySpaceFragment = new ActivitySpaceFragment();
                    transaction.add(R.id.fl_content_main, mActivitySpaceFragment);
                } else {
                    transaction.show(mActivitySpaceFragment);
                }
                break;
            case 4:
                if (mPersonalFragment == null) {
                    mPersonalFragment = new PersonalFragment();
                    transaction.add(R.id.fl_content_main, mPersonalFragment);
                } else {
                    transaction.show(mPersonalFragment);
                }
                break;
        }

        transaction.commit();
    }

    private void setTabSelected(int selectTabIndex) {
        tvHomeTab.setSelected(false);
        tvInformationTab.setSelected(false);
        tvActivityTab.setSelected(false);
        tvActivitySpaceTab.setSelected(false);
        tvPersonalTab.setSelected(false);
        switch (selectTabIndex) {
            case 0:
                tvHomeTab.setSelected(true);
                break;
            case 1:
                tvInformationTab.setSelected(true);
                break;
            case 2:
                tvActivityTab.setSelected(true);
                break;
            case 3:
                tvActivitySpaceTab.setSelected(true);
                break;
            case 4:
                tvPersonalTab.setSelected(true);
                break;
        }
    }

    long lastBackKeyDownTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackKeyDownTime > 2000) { // 两秒钟内双击返回键关闭主界面
            ToastUtil.showShortToast("再按一次退出应用");
            lastBackKeyDownTime = System.currentTimeMillis();
        } else {
            Process.killProcess(Process.myPid());
            super.onBackPressed();
        }
    }

    @OnClick({R.id.tv_home_tab, R.id.tv_information_tab, R.id.tv_activity_tab, R.id.tv_activity_space_tab, R.id.tv_personal_tab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_home_tab:
                setFragmentShow(0);
                setTabSelected(0);
                break;
            case R.id.tv_information_tab:
                setFragmentShow(1);
                setTabSelected(1);
                break;
            case R.id.tv_activity_tab:
                setFragmentShow(2);
                setTabSelected(2);
                break;
            case R.id.tv_activity_space_tab:
                setFragmentShow(3);
                setTabSelected(3);
                break;
            case R.id.tv_personal_tab:
                setFragmentShow(4);
                setTabSelected(4);
                break;
        }
    }
}
