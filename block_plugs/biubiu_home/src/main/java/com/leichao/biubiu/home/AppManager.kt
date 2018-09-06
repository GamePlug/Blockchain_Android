package com.leichao.biubiu.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import com.leichao.biubiu.home.app.copy.AppCopyActivity
import com.leichao.common.proxy.DroidPluginProxy
import com.leichao.common.proxy.RePluginProxy
import com.leichao.util.AppUtil

object AppManager {

    val appList = ArrayList<AppInfo>()
    val emptyApp = AppInfo(AppInfo.AppType.EMPTY, "", ColorDrawable())
    val listeners = ArrayList<OnInstallListener>()

    init {
        // 系统应用
        val defaultIcon = ContextCompat.getDrawable(AppUtil.getApp(), R.mipmap.ic_launcher)!!
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "设置", defaultIcon))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "回收站", defaultIcon))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "关于我们", defaultIcon))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "应用商店", defaultIcon))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, AppInfo.InstallStatus.INSTALLED, "应用分身", defaultIcon, 0, object : AppInfo.Callback() {
            override fun startApp(context: Context) {
                context.startActivity(Intent(context, AppCopyActivity::class.java))
            }
        }))
        // RePlugin应用
        for (reApp in RePluginProxy.getAllInstalled()) {
            appList.add(AppInfo(AppInfo.AppType.PLUGIN_RE, AppInfo.InstallStatus.INSTALLED, reApp.appName, reApp.appIcon, 0, object : AppInfo.Callback() {
                override fun startApp(context: Context) {
                    val intent = context.packageManager.getLaunchIntentForPackage(reApp.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent?.let { context.startActivity(it) }
                }

                override fun installApp(): Boolean {
                    return RePluginProxy.install(reApp.filePath)
                }

                override fun uninstallApp(): Boolean {
                    return RePluginProxy.uninstall(reApp.pluginName)
                }

                override fun isAppInstalled(): Boolean {
                    return RePluginProxy.isInstalled(reApp.pluginName)
                }
            }))
        }
        // DroidPlugin应用
        for (droidApp in DroidPluginProxy.getAllInstalled()) {
            appList.add(AppInfo(AppInfo.AppType.PLUGIN_DROID, AppInfo.InstallStatus.INSTALLED, droidApp.appName, droidApp.appIcon, 0, object : AppInfo.Callback() {
                override fun startApp(context: Context) {
                    val intent = context.packageManager.getLaunchIntentForPackage(droidApp.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent?.let { context.startActivity(it) }
                }

                override fun installApp(): Boolean {
                    return DroidPluginProxy.install(droidApp.filePath)
                }

                override fun uninstallApp(): Boolean {
                    return DroidPluginProxy.uninstall(droidApp.packageName)
                }

                override fun isAppInstalled(): Boolean {
                    return DroidPluginProxy.isInstalled(droidApp.packageName)
                }
            }))
        }
        // 空白填充
        val emptyNum = if (appList.size <= 24) 24 - appList.size else appList.size % 4
        for (i in 1..emptyNum) {
            appList.add(emptyApp)
        }

        addInstallListener(object : SimpleInstallListener() {
            override fun onInstallStart(app: AppInfo) {
                if (!appList.contains(app)) {
                    appList.add(app)
                }
            }

            override fun onInstallFailure(app: AppInfo) {
                if (appList.contains(app)) {
                    appList.remove(app)
                }
            }

            override fun onUnInstallSuccess(app: AppInfo) {
                if (appList.contains(app)) {
                    appList.remove(app)
                }
            }
        })
    }

    fun addInstallListener(listener: OnInstallListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removeInstallListener(listener: OnInstallListener) {
        if (listeners.contains(listener)) listeners.remove(listener)
    }

    interface OnInstallListener {
        fun onStatusChanged(app: AppInfo)
        fun onInstallStart(app: AppInfo)
        fun onInstallSuccess(app: AppInfo)
        fun onInstallFailure(app: AppInfo)
        fun onUnInstallStart(app: AppInfo)
        fun onUnInstallSuccess(app: AppInfo)
        fun onUnInstallFailure(app: AppInfo)
    }

    open class SimpleInstallListener: OnInstallListener {
        override fun onStatusChanged(app: AppInfo) {}
        override fun onInstallStart(app: AppInfo) {}
        override fun onInstallSuccess(app: AppInfo) {}
        override fun onInstallFailure(app: AppInfo) {}
        override fun onUnInstallStart(app: AppInfo) {}
        override fun onUnInstallSuccess(app: AppInfo) {}
        override fun onUnInstallFailure(app: AppInfo) {}
    }

}
