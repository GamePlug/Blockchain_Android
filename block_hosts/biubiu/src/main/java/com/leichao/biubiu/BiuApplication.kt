package com.leichao.biubiu

import android.app.Application
import android.content.Context
import com.leichao.util.AppUtil
import com.morgoo.droidplugin.PluginHelper
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.RePluginConfig

/**
 * 参考:
 * [com.qihoo360.replugin.RePluginApplication]
 * [com.morgoo.droidplugin.PluginApplication]
 */
class BiuApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RePlugin.App.onCreate()// RePlugin
        PluginHelper.getInstance().applicationOnCreate(baseContext)// DroidPlugin
        AppUtil.init(this)// 初始化Util工具
    }

    override fun attachBaseContext(base: Context) {
        PluginHelper.getInstance().applicationAttachBaseContext(base)// DroidPlugin
        super.attachBaseContext(base)
        RePlugin.App.attachBaseContext(this,// RePlugin
                RePluginConfig().setUseHostClassIfNotFound(true)
                        .setCallbacks(HostCallbacks(this))
                        .setEventCallbacks(HostEventCallbacks(this)))
    }

}
