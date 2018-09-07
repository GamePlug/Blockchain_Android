package com.leichao.biubiu.home.app.copy

import android.content.pm.ApplicationInfo
import com.leichao.biubiu.home.AppInfo
import com.leichao.common.proxy.Plugin
import com.leichao.common.proxy.ProxyDroidPlugin
import com.leichao.util.AppUtil
import java.util.*

object AppCopyManager {

    private val appList = ArrayList<AppInfo>()

    fun getAppList(): ArrayList<AppInfo> {
        if (appList.isEmpty()) {
            val packageManager = AppUtil.getApp().packageManager
            val apps = packageManager.getInstalledApplications(0)
            val droidApps = ProxyDroidPlugin.getAllInstalled()
            for (info in apps) {
                if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {//非系统应用
                    val filePath = info.sourceDir
                    val packageName = info.packageName
                    val appName = info.loadLabel(packageManager).toString()
                    val appIcon = info.loadIcon(packageManager)
                    var installStatus = AppInfo.Status.UNINSTALLED
                    droidApps.forEach {
                        if (packageName == it.packageName) {
                            installStatus = AppInfo.Status.INSTALLED
                        }
                    }
                    appList.add(AppInfo(AppInfo.Type.PLUGIN_DROID, installStatus, 0, Plugin(
                            appName, appIcon, packageName, packageName, filePath
                    )))
                }
            }
        }
        return appList
    }

}
