package com.leichao.biubiu;

import android.content.Context;

import com.leichao.util.AppUtil;
import com.morgoo.droidplugin.PluginHelper;
import com.qihoo360.replugin.RePluginApplication;

public class BiuApplication extends RePluginApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // DroidPlugin
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        // 初始化Util工具
        AppUtil.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // DroidPlugin
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }

}
