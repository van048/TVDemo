package cn.ben.tvdemo.application;

import android.app.Application;

import cn.ben.tvdemo.data.DBOpenHelper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBOpenHelper.initialize(getApplicationContext());
    }
}
