package com.zbyj.Yazhou;

import android.os.Bundle;
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

public class Debugmap extends YazhouActivity {
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_debugmap);
        MapView mapView = findViewById(R.id.activity_debugmap_mapview);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        LocationClient locationClient = new LocationClient(this);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setScanSpan(1000);
        locationClient.setLocOption(locationClientOption);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null && baiduMap != null) {
                    MyLocationData myLocationData = new MyLocationData.Builder()
                            .accuracy(bdLocation.getRadius())
                            .direction(bdLocation.getDirection())
                            .latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude()).build();
                    baiduMap.setMyLocationData(myLocationData);
                    Toast.makeText(getApplicationContext(), bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getStreet(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "定位到的经度" + bdLocation.getLongitude() + "定位到的维度" + bdLocation.getLatitude(), Toast.LENGTH_LONG).show();
                    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    mapStatusUpdate = MapStatusUpdateFactory.zoomTo(19);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                } else {
                    return;
                }
            }
        });
        locationClient.start();//开启定位
    }
}
