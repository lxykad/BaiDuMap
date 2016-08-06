package com.eyes_control.ah100;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener, InfoWindow.OnInfoWindowClickListener {

    MapView mMapView = null;
    private Marker mMarker;
    private Marker mMarker2;
    private Marker mMarker3;
    private Marker mMarker4;

    private BaiduMap mMap;
    private LatLng mLatLng;
    private LatLng mLatLng2;
    private LatLng mLatLng3;
    private LatLng mLatLng4;

    private BitmapDescriptor mBitmap;
    private MarkerOptions mOptions;
    private MarkerOptions mOptions2;
    private MarkerOptions mOptions3;
    private MarkerOptions mOptions4;

    private View mPop;
    private TextView mTvDetail;
    private TextView mTvCancel;
    private ArrayList<MarkerOptions> mOptionList = new ArrayList<>();

    private BitmapDescriptor icon;//标注图标

    //轨迹回放
    private List<LatLng> points = new ArrayList<LatLng>();// 所有路线点集合
    private List<LatLng> points2 = new ArrayList<LatLng>();
    private int index = 0;
    private boolean flag;
    private Button mBtGuiJi;

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                flag = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        start();
                    }
                }).start();
            }
            if (msg.what == 2) {
                flag = false;
            }
            if (msg.what == 3) {
                mBtGuiJi.setText("重新开始");
                points2.clear();
                flag = false;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化轨迹数据
        initLocation();


        mBtGuiJi = (Button) findViewById(R.id.guiji);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMap = mMapView.getMap();

        //设置地图缩放级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);//500m
        mMap.setMapStatus(msu);


        mLatLng = new LatLng(20.963175, 76.400244);
        mLatLng2 = new LatLng(21.963175, 77.400244);
        mLatLng3 = new LatLng(25.963175, 75.400244);
        mLatLng4 = new LatLng(24.963175, 74.400244);
        mBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.lgz);
        mOptions = new MarkerOptions().position(mLatLng).icon(mBitmap);
        mOptions2 = new MarkerOptions().position(mLatLng2).icon(mBitmap);
        mOptions3 = new MarkerOptions().position(mLatLng3).icon(mBitmap);
        mOptions4 = new MarkerOptions().position(mLatLng4).icon(mBitmap);

        mMarker = (Marker) mMap.addOverlay(mOptions);
        mMarker2 = (Marker) mMap.addOverlay(mOptions);
        mMarker3 = (Marker) mMap.addOverlay(mOptions);
        mMarker4 = (Marker) mMap.addOverlay(mOptions);

        //mMap.addOverlay(options);
        mPop = LayoutInflater.from(this).inflate(R.layout.map_info_window, null);
        mTvDetail = (TextView) mPop.findViewById(R.id.more_detail);
        mTvCancel = (TextView) mPop.findViewById(R.id.i_know);
        mMap.setOnMarkerClickListener(this);

        //
        mOptionList.add(mOptions);
        mOptionList.add(mOptions2);
        mOptionList.add(mOptions3);
        mOptionList.add(mOptions4);

        //初始化线路轨迹
        initOverlay();
        LatLng l = new LatLng(points.get(0).latitude, points.get(0).longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(l);
        mMap.setMapStatus(u);//移动地图中心到此点

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

        //
        mMap.clear();
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
        showPopWindow(marker);
        return false;
    }

    @Override
    public void onInfoWindowClick() {
        Toast.makeText(MainActivity.this, "window", Toast.LENGTH_SHORT).show();

    }

    //弹窗popupWindow
    public void showPopWindow(Marker marker) {
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
        LatLng latLng = marker.getPosition();
        InfoWindow infoWindow = new InfoWindow(mPop, latLng, -47);
        mTvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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

    //初始化marker
    public void initMark(View view) {
        mMap.clear();
        Toast.makeText(MainActivity.this, "init4", Toast.LENGTH_SHORT).show();
        //定位到指定位置并添加marker
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLatLng);
//        mMap.setMapStatus(u);
//        mMap.addOverlay(mOptions);
        for (int i = 0; i < 4; i++) {
            mMap.addOverlay(mOptionList.get(i));
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLatLng3);
        mMap.setMapStatus(u);

    }

    //轨迹回放
    public void showGuiJi(View view) {

        resetOverlay(view);
    }

    // 初始化
    public void initOverlay() {
        OverlayOptions ooPolyline = new PolylineOptions().width(5)
                .color(0xAAFF0000).points(points);
        mMap.addOverlay(ooPolyline);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points.get(0));
        mMap.setMapStatus(u);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.car);
        OverlayOptions ooA = new MarkerOptions().position(points.get(index))
                .icon(icon).draggable(true);
        mMarker = (Marker) (mMap.addOverlay(ooA));
    }

    //开始回放轨迹
    public void resetOverlay(View view) {
        Button button = (Button) view;
        if (!flag) {
            if (index == points.size()) {
                mMap.clear();
                index = 0;
                initOverlay();// 初始化
            }
            button.setText("暂停");
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        } else {
            button.setText("继续");
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    // 画回放轨迹
    public void start() {
        if (flag) {
            if (mMarker != null) {
                mMarker.remove();
            }
            /**
             * 计算角度
             */
            Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.car);
            Matrix matrix = new Matrix();
            double x1 = points.get(index).latitude, x2 = points
                    .get(index + 1 == points.size() ? index - 2 : index + 1).latitude; // 点1坐标;
            double y1 = points.get(index).longitude, y2 = points
                    .get(index + 1 == points.size() ? index - 2 : index + 1).longitude;// 点2坐标
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            int jiaodu = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 最终角度
            x = y1 - y2;
            y = x1 - x2;

            if (x > 0 && y < 0) {// 在第二象限
                jiaodu = 0 - jiaodu;
            }
            if (x > 0 && y > 0) {// 在第三象限
                jiaodu = jiaodu + 90;
            }
            if (x < 0 && y > 0) {// 在第四象限
                jiaodu = 180 + (90 - jiaodu);
            }
            /**
             * 计算角度
             */


            /**
             * 旋转图标
             */
            matrix.postRotate(jiaodu);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                    bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
            /**
             * 旋转图标
             */

            OverlayOptions ooA = new MarkerOptions()
                    .position(points.get(index)).icon(icon).draggable(true);
            mMarker = (Marker) (mMap.addOverlay(ooA));
            if (points2.size() <= 1) {
                points2.add(points.get(index));
                points2.add(points.get(index + 1));
            } else {
                points2.add(points.get(index));
            }
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points
                    .get(index));
            mMap.setMapStatus(u);
            OverlayOptions s = new PolylineOptions().width(10)
                    .color(0x2427D600).points(points2);
            mMap.addOverlay(s);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            index++;
            if (index != points.size()) {
                start();
            } else {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        }
    }

    //初始化位置信息
    private void initLocation() {
        points.add(new LatLng(32.544050387869, 117.70401156231));
        points.add(new LatLng(32.544120468483, 117.70343449085));
        points.add(new LatLng(32.544249215785, 117.70278780071));
        points.add(new LatLng(32.54431556775, 117.70257894468));
        points.add(new LatLng(32.544524819609, 117.70197213931));
        points.add(new LatLng(32.5446104225, 117.70172348834));
        points.add(new LatLng(32.54519700934, 117.69987315865));
        points.add(new LatLng(32.545613453971, 117.69859001906));
        points.add(new LatLng(32.546020081015, 117.69732682185));
        points.add(new LatLng(32.546347800972, 117.69612327213));
        points.add(new LatLng(32.546832264271, 117.69637785191));
        points.add(new LatLng(32.547013585415, 117.69644756042));
        points.add(new LatLng(32.549426042119, 117.69764239658));
        points.add(new LatLng(32.549487901074, 117.69666737578));
        points.add(new LatLng(32.549761358224, 117.69361322688));
        points.add(new LatLng(32.549848325639, 117.69234976018));
        points.add(new LatLng(32.549874195195, 117.69212096176));
        points.add(new LatLng(32.550114705616, 117.69215060584));
        points.add(new LatLng(32.550796363856, 117.69224034657));
        points.add(new LatLng(32.552080231919, 117.69244956192));
        points.add(new LatLng(32.554165774207, 117.69274833833));
        points.add(new LatLng(32.555359588385, 117.69295737402));
        points.add(new LatLng(32.55645197023, 117.69308673002));
        points.add(new LatLng(32.557896285667, 117.69332540979));
        points.add(new LatLng(32.558447331773, 117.69338505728));
        points.add(new LatLng(32.558577123441, 117.69337517592));
        points.add(new LatLng(32.558855649941, 117.69329558605));
        points.add(new LatLng(32.559272334909, 117.69311655376));
        points.add(new LatLng(32.559808841508, 117.69292746045));
        points.add(new LatLng(32.559908123783, 117.69288775535));
        points.add(new LatLng(32.565630095042, 117.69080620182));
        points.add(new LatLng(32.56797464725, 117.68994921836));
        points.add(new LatLng(32.571562027083, 117.68867299572));
        points.add(new LatLng(32.573151765341, 117.68808460561));
        points.add(new LatLng(32.573251413043, 117.68806475306));
        points.add(new LatLng(32.573390919637, 117.68803483949));
        points.add(new LatLng(32.575914703664, 117.6876943817));
        points.add(new LatLng(32.580842946067, 117.68705281191));
        points.add(new LatLng(32.581441159125, 117.68695274067));
        points.add(new LatLng(32.581630928351, 117.68694267965));
        points.add(new LatLng(32.583298826474, 117.68684153045));
        points.add(new LatLng(32.587013436137, 117.68656907439));
        points.add(new LatLng(32.587063404335, 117.68656907439));
        points.add(new LatLng(32.587622483431, 117.68651885911));
        points.add(new LatLng(32.587841292235, 117.68943350117));
        points.add(new LatLng(32.58802922248, 117.69102556802));
        points.add(new LatLng(32.58814535724, 117.69247821786));
        points.add(new LatLng(32.588285600979, 117.69412975254));
        points.add(new LatLng(32.588476876653, 117.69635817902));
        points.add(new LatLng(32.589048647763, 117.70247671751));
        points.add(new LatLng(32.589119681586, 117.70310364493));
        points.add(new LatLng(32.589133827501, 117.70334250436));
        points.add(new LatLng(32.589087511028, 117.70355154006));
        points.add(new LatLng(32.589474925902, 117.70457632698));
        points.add(new LatLng(32.589569231584, 117.70483503897));
        points.add(new LatLng(32.589925006469, 117.70579029705));
        points.add(new LatLng(32.590124492277, 117.70637742953));
        points.add(new LatLng(32.590216515697, 117.70650678552));
        points.add(new LatLng(32.590521333088, 117.70681508397));
        points.add(new LatLng(32.590899387335, 117.70733241812));
        points.add(new LatLng(32.591400339054, 117.70804890659));
        points.add(new LatLng(32.591633588267, 117.70893499515));
        points.add(new LatLng(32.591675948753, 117.70909426471));
        points.add(new LatLng(32.591888511388, 117.70997056174));
        points.add(new LatLng(32.592078106182, 117.71066773664));
        points.add(new LatLng(32.592284888009, 117.71117563858));
        points.add(new LatLng(32.592422843694, 117.71178334226));
        points.add(new LatLng(32.592544676485, 117.7127299766));
        points.add(new LatLng(32.592550456313, 117.71320841412));
        points.add(new LatLng(32.592557833199, 117.71384639069));
        points.add(new LatLng(32.592573575624, 117.7143648028));
        points.add(new LatLng(32.592665748605, 117.71456413679));
        points.add(new LatLng(32.592759138295, 117.71488312508));
        points.add(new LatLng(32.592823933037, 117.71535177106));
        points.add(new LatLng(32.593040371471, 117.71600977985));
        points.add(new LatLng(32.593355826122, 117.7165979903));
        points.add(new LatLng(32.593647932478, 117.71683711923));
        points.add(new LatLng(32.593832047966, 117.71731582623));
        points.add(new LatLng(32.593894712548, 117.71763499418));
        points.add(new LatLng(32.594015858876, 117.71777459086));
        points.add(new LatLng(32.594206285665, 117.71783423834));
        points.add(new LatLng(32.594626607246, 117.71788373498));
        points.add(new LatLng(32.59528663055, 117.71790304854));
        points.add(new LatLng(32.595936002301, 117.71784268241));
    }

}


