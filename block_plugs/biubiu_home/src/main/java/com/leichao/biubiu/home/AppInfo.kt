package com.leichao.biubiu.home

data class AppInfo(
        var appType: AppType,
        var appName: String,
        var appIcon: Int,
        var appSort: Int
) {
    constructor(appType: AppType, appName: String, appIcon: Int) :
            this(appType, appName, appIcon, 0)

    enum class AppType {
        SYSTEM, PLUGIN_RE, PLUGIN_DROID, EMPTY
    }
}
