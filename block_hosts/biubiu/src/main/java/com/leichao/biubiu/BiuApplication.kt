package com.leichao.biubiu

import android.content.Context

import com.leichao.util.AppUtil
import com.morgoo.droidplugin.PluginHelper
import com.qihoo360.replugin.RePluginApplication

/**
 * 参考:com.morgoo.droidplugin.PluginApplication
 */
class BiuApplication : RePluginApplication() {

    override fun onCreate() {
        super.onCreate()
        // DroidPlugin
        PluginHelper.getInstance().applicationOnCreate(baseContext)
        // 初始化Util工具
        AppUtil.init(this)
    }

    override fun attachBaseContext(base: Context) {
        // DroidPlugin
        PluginHelper.getInstance().applicationAttachBaseContext(base)
        super.attachBaseContext(base)
    }

}
