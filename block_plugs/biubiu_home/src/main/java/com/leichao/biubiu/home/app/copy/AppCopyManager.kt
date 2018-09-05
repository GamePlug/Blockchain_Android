package com.leichao.biubiu.home.app.copy

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import com.leichao.biubiu.home.AppInfo
import com.leichao.common.proxy.DroidPluginProxy
import com.leichao.util.AppUtil
import java.util.*

object AppCopyManager {

    val appList = ArrayList<AppInfo>()

    init {
        val packageManager = AppUtil.getApp().packageManager
        val apps = packageManager.getInstalledApplications(0)
        for (info in apps) {
            if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {//非系统应用
                val filePath = info.sourceDir
                val packageName = info.packageName
                val appName = info.loadLabel(packageManager).toString()
                val appIcon = info.loadIcon(packageManager)
                appList.add(AppInfo(AppInfo.AppType.PLUGIN_DROID, appName, appIcon, 0, object : AppInfo.Callback() {
                    override fun startApp(context: Context) {
                        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent?.let { context.startActivity(it) }
                    }

                    override fun installApp(): Boolean {
                        return DroidPluginProxy.install(filePath)
                    }

                    override fun uninstallApp(): Boolean {
                        return DroidPluginProxy.uninstall(packageName)
                    }

                    override fun isAppInstalled(): Boolean {
                        return DroidPluginProxy.isInstalled(packageName)
                    }
                }))
            }
        }
    }

}
