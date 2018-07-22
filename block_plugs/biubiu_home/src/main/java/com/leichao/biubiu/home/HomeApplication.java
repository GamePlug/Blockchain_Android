package com.leichao.biubiu.home;

import android.app.Application;
import android.content.Context;

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
        Context hostContext = RePlugin.getHostContext();
        if (hostContext == null) {
            hostContext = getBaseContext();
        }
        PluginHelper.getInstance().applicationOnCreate(hostContext);
    }

    @Override
    protected void attachBaseContext(Context base) {
        Context hostContext = RePlugin.getHostContext();
        if (hostContext == null) {
            hostContext = base;
        }
        PluginHelper.getInstance().applicationAttachBaseContext(hostContext);
        super.attachBaseContext(base);
    }

}
