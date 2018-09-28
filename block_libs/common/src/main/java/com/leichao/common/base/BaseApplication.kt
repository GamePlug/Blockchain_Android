package com.leichao.common.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)// 解决方法数量过多的问题
    }

}
