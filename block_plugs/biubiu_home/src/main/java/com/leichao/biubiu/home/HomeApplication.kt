package com.leichao.biubiu.home

import com.leichao.common.base.BaseApplication

import com.leichao.util.AppUtil
import com.qihoo360.replugin.RePlugin

class HomeApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        AppUtil.init(this, RePlugin.getHostContext()) // 初始化Util工具
    }

}
