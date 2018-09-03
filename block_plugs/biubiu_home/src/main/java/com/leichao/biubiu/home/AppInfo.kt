package com.leichao.biubiu.home

import android.content.Context

data class AppInfo(
        var appType: AppType,
        var appName: String,
        var appIcon: Int,
        var appSort: Int,
        var callback: Callback?
) {
    constructor(appType: AppType, appName: String, appIcon: Int) :
            this(appType, appName, appIcon, 0, null)

    enum class AppType {
        SYSTEM, PLUGIN_RE, PLUGIN_DROID, EMPTY
    }

    abstract class Callback {
        abstract fun startActivity(context: Context)
    }

}
