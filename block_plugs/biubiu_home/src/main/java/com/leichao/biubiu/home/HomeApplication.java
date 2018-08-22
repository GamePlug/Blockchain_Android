package com.leichao.biubiu.home;

import android.app.Application;

import com.leichao.util.AppUtil;

public class HomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Util工具
        AppUtil.init(this);
    }

}
