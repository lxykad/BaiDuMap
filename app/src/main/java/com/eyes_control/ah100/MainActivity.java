package com.eyes_control.ah100;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener, InfoWindow.OnInfoWindowClickListener {

    MapView mMapView = null;
    private Marker mMarker;
    private BaiduMap mMap;
    private LatLng mLatLng;
    private BitmapDescriptor mBitmap;
    private MarkerOptions mOptions;

    private View mPop;
    private TextView mTvDetail;
    private TextView mTvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();
        mLatLng = new LatLng(19.963175, 76.400244);
        mBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.lgz);
        mOptions = new MarkerOptions().position(mLatLng).icon(mBitmap);
        mMarker = (Marker) mMap.addOverlay(mOptions);
        //mMap.addOverlay(options);
        mPop = LayoutInflater.from(this).inflate(R.layout.map_info_window, null);
        mTvDetail = (TextView) mPop.findViewById(R.id.more_detail);
        mTvCancel = (TextView) mPop.findViewById(R.id.i_know);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //标记覆盖物
    public void clickMark(View view) {
//        //定义Maker坐标点
//        LatLng point = new LatLng(39.963175, 116.400244);
//        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.mipmap.lgz);
//        //构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
//        //在地图上添加Marker，并显示
//        mMapView.getMap().addOverlay(option);

        //定位到指定位置并添加marker
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLatLng);
        mMap.setMapStatus(u);
        mMap.addOverlay(mOptions);

    }

    //delMark 删除标记
    public void delMark(View view) {
        mMap.clear();
        // mMarker.remove();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {//marker的点击事件
        //show();
        //弹出自定义弹窗
        showPopWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick() {
        Toast.makeText(MainActivity.this, "window", Toast.LENGTH_SHORT).show();

    }

    //弹窗popupWindow
    public void showPopWindow() {
        //LatLng llInfo = mMap.getProjection().fromScreenLocation(p);
//        new InfoWindow(mPop,mLatLng, -47, new InfoWindow.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick() {
//
//            }
//        });
//        InfoWindow infoWindow = new InfoWindow(mPop, mLatLng, -47, new InfoWindow.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick() {
//
//            }
//        });
        InfoWindow infoWindow = new InfoWindow(mPop,mLatLng,-47);
        mTvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.hideInfoWindow();
            }
        });

        mMap.showInfoWindow(infoWindow);

    }
}


