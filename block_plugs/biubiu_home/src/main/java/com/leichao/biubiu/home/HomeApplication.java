package com.leichao.biubiu.home;

import android.app.Application;
import android.content.Context;

import com.leichao.util.AppUtil;
import com.morgoo.droidplugin.PluginHelper;
import com.qihoo360.replugin.RePlugin;

/**
 * 参考:com.morgoo.droidplugin.PluginApplication
 * 此处需要使用RePlugin.getHostContext()获取主程序Context
 */
public class HomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // DroidPlugin
        Context hostContext = RePlugin.getHostContext();
        if (hostContext == null) {
            hostContext = getBaseContext();
        }
        PluginHelper.getInstance().applicationOnCreate(hostContext);
        // 初始化Util工具
        AppUtil.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // DroidPlugin
        Context hostContext = RePlugin.getHostContext();
        if (hostContext == null) {
            hostContext = base;
        }
        PluginHelper.getInstance().applicationAttachBaseContext(hostContext);
        super.attachBaseContext(base);
    }

}
