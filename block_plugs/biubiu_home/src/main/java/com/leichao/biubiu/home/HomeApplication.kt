package com.leichao.biubiu.home

import android.app.Application

import com.leichao.util.AppUtil
import com.qihoo360.replugin.RePlugin

class HomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化Util工具
        AppUtil.init(this, RePlugin.getHostContext())
    }

}
