package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.synchronization.DisplayOptions;
import com.zbyj.Yazhou.ConfigPageValue.MAP;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;

public class InputAddrAct extends YazhouActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private ListView listView;//显示地址的listview

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//地图初始化  不然会闪屏直接退出
        setContentView(R.layout.activity_inputaddr);
        setStatusBar(this.getResources().getString(R.color.TextAndBodyColor));
        init();
    }

    /**
     * 地图控件初始化
     */
    private void init() {
        listView = findViewById(R.id.activity_inputaddr_listview);
        mapView = findViewById(R.id.activity_inputaddr_mapview);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(this.getApplicationContext());
        locationClientOption = new LocationClientOption();
        locationClientOption.setIsNeedAddress(true);//是否需要地址位置信息
        locationClientOption.setOpenGps(true);//打开GPS定位
        locationClientOption.setCoorType("bd09ll");//坐标类型
        locationClient.setLocOption(locationClientOption);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (mapView == null) {
                    locationClient.stop();
                } else {
                    /**
                     * 这个可以显示蓝点位置
                     *
                     * accuracy 是误差半径
                     */
                    MyLocationData myLocationData = new MyLocationData.Builder()
                            .accuracy(300)
                            .direction(bdLocation.getDirection())
                            .latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude()).build();
                    baiduMap.setMyLocationData(myLocationData);
                    Toast.makeText(getApplicationContext(), bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getStreet(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "定位到的经度" + bdLocation.getLongitude() + "定位到的维度" + bdLocation.getLatitude(), Toast.LENGTH_LONG).show();
                    /**
                     * 这个是移动到该位置的调用
                     */
                    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    mapStatusUpdate = MapStatusUpdateFactory.zoomTo(19);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    /**
                     * 定位成功 显示位置信息
                     */

                    listView.setAdapter(new showAddrAdapter());
                }
            }
        });
        locationClient.start();
        Linstener();
    }


    //地图的生命周期的管理
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 按钮监听
     */
    private void Linstener() {
    }

    /**
     * 显示地址的adapter
     */
    public class showAddrAdapter extends BaseAdapter {
        /**
         * 实例化该方法 要传入对应要显示的值  为地址信息
         */
        public showAddrAdapter() {

        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_showaddr_list, null);
                convertView.setTag("" + position);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout lv = (LinearLayout) v;
                        Toast.makeText(getApplicationContext(), lv.getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return convertView;
        }
    }

    /**
     * 向哪里来的界面返回数据信息
     */
    public void gotoBackData(String addr) {
        Intent i = new Intent();
        i.putExtra(MAP.GET_USERADDR_ONSUCESS, addr);
        setResult(MAP.SET_USERADDR_SUCESS, i);
    }
}
