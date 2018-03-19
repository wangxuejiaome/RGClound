package com.rgcloud.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.rgcloud.R;
import com.rgcloud.adapter.SpaceMapNavigationAdapter;
import com.rgcloud.config.Constant;
import com.rgcloud.entity.request.SpaceReqEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.http.RequestApi;
import com.rgcloud.http.ResponseCallBack;
import com.rgcloud.util.BNEventHandler;
import com.rgcloud.util.CirCleLoadingDialogUtil;
import com.rgcloud.util.OverlayManager;
import com.rgcloud.util.ToastUtil;
import com.rgcloud.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rgcloud.R.drawable.position;

public class MapActivity extends BaseActivity {

    private static final String APP_FOLDER_NAME = Constant.BD_NAVIGATION_FOLDER_NAME;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    @Bind(R.id.tb_map)
    TitleBar tbMap;
    @Bind(R.id.rv_space_navigation)
    RecyclerView rvSpaceNavigation;
    @Bind(R.id.map_view)
    MapView mapView;
    @Bind(R.id.tv_name_space)
    TextView tvNameSpace;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.btn_go_navigation)
    Button btnGoNavigation;
    @Bind(R.id.tv_address_space)
    TextView tvAddressSpace;
    @Bind(R.id.tv_phone_space)
    TextView tvPhoneSpace;
    @Bind(R.id.btn_go_space)
    Button btnGoSpace;
    @Bind(R.id.ll_space_information)
    LinearLayout llSpaceInformation;
    @Bind(R.id.activity_map)
    RelativeLayout activityMap;


    private final static String authBaseArr[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    // 定位相关
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mLocationMode;
    private BitmapDescriptor mCurrentMarker;
    private boolean isFirstLoc = true; // 是否首次定位

    public static List<Activity> activityList = new LinkedList<Activity>();


    // 初始化全局 bitmap 信息，不用时及时 recycle

    BitmapDescriptor mBDMark = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_location_red_128);

    private String mSDCardPath = null;
    private BNRoutePlanNode.CoordinateType mCoordinateType = BNRoutePlanNode.CoordinateType.BD09LL;

    private SpaceMapNavigationAdapter mSpaceMapNavigationAdapter;
    List<Marker> mMarkerList = new ArrayList<>();
    private int mSpaceType = 1;

    private double mCurrentLat;
    private double mCurrentLng;
    private double mSelectedLat;
    private double mSelectLng;
    private int mSelectedSpaceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mBaiduMap = mapView.getMap();
        BNOuterLogUtil.setLogSwitcher(true);
        activityList.add(this);

        initTitleBar(R.id.tb_map, "文化地图");

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mLocationMode, true, mCurrentMarker));

        if (initDirs()) {
            initNavi();
        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                if (llSpaceInformation.getVisibility() == View.GONE) {
                    llSpaceInformation.setVisibility(View.VISIBLE);
                }
                Bundle bundle = marker.getExtraInfo();
                tvNameSpace.setText(bundle.getString("spaceName"));
                tvAddressSpace.setText(bundle.getString("spaceAddress"));
                tvPhoneSpace.setText(bundle.getString("spacePhone"));
                // latlngToAddress(marker.getPosition(), tvAddressSpace);
                mSelectedLat = marker.getPosition().latitude;
                mSelectLng = marker.getPosition().longitude;
                mSelectedSpaceId = bundle.getInt("spaceId");
                return true;
            }
        });

        rvSpaceNavigation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSpaceType = ((ActivitySpaceResEntity.SpaceTypeBean) adapter.getItem(position)).TypeId;
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    ActivitySpaceResEntity.SpaceTypeBean spaceTypeBean = (ActivitySpaceResEntity.SpaceTypeBean) adapter.getItem(i);
                    spaceTypeBean.hasSelected = i == position;
                }
                mSpaceMapNavigationAdapter.notifyDataSetChanged();
                getActivitySpace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    public void initOverlay(List<ActivitySpaceResEntity.ActivitySpaceBean> spaceBeanList) {


        MarkerOptions markerOptions;

        for (int i = 0; i < spaceBeanList.size(); i++) {
            ActivitySpaceResEntity.ActivitySpaceBean spaceBean = spaceBeanList.get(i);
            markerOptions = new MarkerOptions().position(new LatLng(spaceBean.Longitude, spaceBean.Latitude)).icon(mBDMark).zIndex(9);
            markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);
            mBaiduMap.addOverlay(markerOptions);
            Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
            Bundle bundle = new Bundle();
            bundle.putInt("spaceId", spaceBean.Id);
            bundle.putString("spaceName", spaceBean.SpaceName);
            bundle.putString("spaceAddress", spaceBean.SpaceAddress);
            bundle.putString("spacePhone",spaceBean.SpacePhone);
            marker.setExtraInfo(bundle);
            mMarkerList.add(marker);
        }

        OverlayManager overlayManager = new OverlayManager(mBaiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return null;
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }

            @Override
            public boolean onPolylineClick(Polyline polyline) {
                return false;
            }
        };

        overlayManager.zoomToSpan();

    }

    /**
     * 清除所有Overlay
     */
    public void clearOverlay() {
        mBaiduMap.clear();
        mMarkerList.clear();
    }

    //将经纬度转换成地址
    private void latlngToAddress(LatLng latlng, final TextView tvAddress) {
        GeoCoder geoCoder = GeoCoder.newInstance();

        //设置地址或经纬度反编译后的监听,这里有两个回调方法,
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            //经纬度转换成地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.d("onGetGeoCodeResult", result.toString());
                    return;
                }
                tvAddress.setText(result.getAddress());

                Log.d("onGetGeoCodeResult", result.getAddressDetail().toString());
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                ToastUtil.showShortToast("获取地址失败");
            }
        });
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                Toast.makeText(mContext, "定位失败，请稍候再试", Toast.LENGTH_SHORT).show();
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mCurrentLat = location.getLatitude();
            mCurrentLng = location.getLongitude();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
               /* MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(13f);
                //builder.target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/

                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(ll)
                        .zoom(13)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                getActivitySpace();
            }

        }
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };


    private boolean hasBasePhoneAuth() {

        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {

        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {

        // 申请权限
        if (Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(final int status, String msg) {
                if (0 == status) {
                   // authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MapActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if(0 != status){
                            Toast.makeText(MapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            public void initSuccess() {
               // Toast.makeText(MapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
              //  Toast.makeText(MapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(MapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    private void routeplanToNavi() {
        if (!hasInitSuccess) {
            Toast.makeText(MapActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(MapActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        sNode = new BNRoutePlanNode(mCurrentLng, mCurrentLat, "", null, mCoordinateType);
        eNode = new BNRoutePlanNode(mSelectLng, mSelectedLat, "", null, mCoordinateType);


        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode), eventListerner);
        }
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(MapActivity.this, BNGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(MapActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10150203");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "stopTTS");
        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneCalling");
        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "getTTSState");
            return 1;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(MapActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            routeplanToNavi();
        }

    }


    private void getActivitySpace() {
        final SpaceReqEntity spaceReqEntity = new SpaceReqEntity();
        spaceReqEntity.type = mSpaceType;
        RequestApi.getActivitySpace(spaceReqEntity, new ResponseCallBack(mContext) {
            @Override
            public void onObjectResponse(Object resEntity) {
                super.onObjectResponse(resEntity);
                if (resEntity == null) return;
                ActivitySpaceResEntity spaceResEntity = (ActivitySpaceResEntity) resEntity;

                //只有第一次的时候才设置导航type，只需设置一次，默认第一个tab被选中
                if (mSpaceMapNavigationAdapter == null) {
                    rvSpaceNavigation.setLayoutManager(new GridLayoutManager(mContext, spaceResEntity.TypeData.size()));
                    spaceResEntity.TypeData.get(0).hasSelected = true;
                    mSpaceMapNavigationAdapter = new SpaceMapNavigationAdapter(spaceResEntity.TypeData);
                    rvSpaceNavigation.setAdapter(mSpaceMapNavigationAdapter);
                    initOverlay(spaceResEntity.DataList);
                }

                clearOverlay();
                initOverlay(spaceResEntity.DataList);
                CirCleLoadingDialogUtil.dismissCircleProgressDialog();
            }
        });
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }


    @OnClick({R.id.iv_close, R.id.btn_go_navigation, R.id.btn_go_space})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                llSpaceInformation.setVisibility(View.GONE);
                break;
            case R.id.btn_go_navigation:
                routeplanToNavi();
                break;
            case R.id.btn_go_space:
                ActivitiesActivity.startActivitiesActivity(mContext, 1, 0, mSelectedSpaceId);
                break;
        }
    }
}
