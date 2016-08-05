package com.eyes_control.ah100;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by user on 2016/8/5.
 */
public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
