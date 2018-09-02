package com.leichao.biubiu.home

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

import com.leichao.util.AppUtil
import com.qihoo360.replugin.RePlugin

class HomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppUtil.init(this, RePlugin.getHostContext()) // 初始化Util工具
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)// 解决方法数量过多的问题
    }

}
