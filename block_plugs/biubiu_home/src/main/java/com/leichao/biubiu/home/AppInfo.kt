package com.leichao.biubiu.home

import android.content.Context
import android.graphics.drawable.Drawable

data class AppInfo(
        var appType: AppType,
        var appName: String,
        var appIcon: Drawable,
        var appSort: Int,
        var callback: Callback
) {
    constructor(appType: AppType, appName: String, appIcon: Drawable) :
            this(appType, appName, appIcon, 0, Callback())

    enum class AppType {
        SYSTEM, PLUGIN_RE, PLUGIN_DROID, EMPTY
    }

    open class Callback {
        open fun startApp(context: Context) {}
        open fun installApp(): Boolean {return true}
        open fun uninstallApp(): Boolean {return true}
        open fun isAppInstalled(): Boolean {return true}
    }

}
