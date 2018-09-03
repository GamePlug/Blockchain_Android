package com.leichao.biubiu.home

import java.util.*

object AppManager {

    val appList = ArrayList<AppInfo>()
    val emptyApp = AppInfo(AppInfo.AppType.EMPTY, "", 0)

    init {
        // 系统应用
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "设置", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "回收站", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "关于我们", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "应用商店", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "应用分身", R.mipmap.ic_launcher))
        for (i in 1..15) {
            appList.add(AppInfo(AppInfo.AppType.PLUGIN_RE, "应用$i", R.mipmap.ic_launcher))
        }
        // 空白填充
        val emptyNum = if (appList.size <= 24) 24 - appList.size else appList.size % 4
        for (i in 1..emptyNum) {
            appList.add(emptyApp)
        }
    }

}
