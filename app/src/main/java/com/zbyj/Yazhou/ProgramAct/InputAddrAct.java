package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zbyj.Yazhou.ConfigPageValue.MAP;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyAct.LeftCompanyAct;
import com.zbyj.Yazhou.config;

import java.util.ArrayList;
import java.util.List;

public class InputAddrAct extends LeftCompanyAct {
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
        // locationClientOption.setOpenGps(true);//打开GPS定位
        locationClientOption.setCoorType("bd09ll");//坐标类型
        locationClient.setLocOption(locationClientOption);
        List<String> list = new ArrayList<String>();
        list.add("龙岩市上杭县临城镇上杭大道");
        list.add("龙岩市新罗区罗龙路好运来精品酒店");
        list.add("厦门市附件胜利科技离开家第三方");
        list.add("龙岩市上杭县北园路贞三巷28号");
        showAddrAdapter adapter  = new showAddrAdapter(list);
        listView.setAdapter(adapter);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.i(config.DEBUG_STR, "调用开始");
                if (mapView == null) {
                    Log.e(config.DEBUG_STR, "mapView的控件为空");
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
                    mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    /**
                     * 定位成功 显示位置信息
                     */
                    //listView.setAdapter(new showAddrAdapter());
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view;
                TextView tv  = layout.findViewById(R.id.item_showaddr_list_addr);
                gotoBackData(tv.getText().toString());
                finish();
            }
        });
    }

    /**
     * 显示地址的adapter
     */
    public class showAddrAdapter extends BaseAdapter {
        private List<String> mList;
        /**
         * 实例化该方法 要传入对应要显示的值  为地址信息
         */
        public showAddrAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return this.mList.size();
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
            Viewpage viewpage;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_showaddr_list, null);
                convertView.setTag("" + position);
                viewpage = new Viewpage();
                viewpage.title = convertView.findViewById(R.id.item_showaddr_list_addr);
                convertView.setTag(viewpage);
            }
            else{
                //以前存在
                viewpage = (Viewpage) convertView.getTag();

            }
            viewpage.title.setText(mList.get(position));
            return convertView;
        }

        public class Viewpage{
            TextView title;
        }
    }
    public  void getPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
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
