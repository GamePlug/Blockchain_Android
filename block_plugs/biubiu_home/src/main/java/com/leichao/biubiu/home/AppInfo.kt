package com.leichao.biubiu.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper

data class AppInfo(
        var appType: AppType,
        var installStatus: InstallStatus,
        var appName: String,
        var appIcon: Drawable,
        var appSort: Int,
        private var callback: Callback
) {
    constructor(appType: AppType, appName: String, appIcon: Drawable) :
            this(appType, InstallStatus.UNINSTALLED, appName, appIcon, 0, Callback())

    private val handler = Handler(Looper.getMainLooper())

    enum class AppType {
        //系统---RePlugin---DroidPlugin----空
        SYSTEM, PLUGIN_RE, PLUGIN_DROID, EMPTY
    }

    enum class InstallStatus {
        //已安装------安装中-------未安装--------卸载中
        INSTALLED, INSTALLING, UNINSTALLED, UNINSTALLING
    }

    open class Callback {
        open fun startApp(context: Context) {}
        open fun installApp(): Boolean {return true}
        open fun uninstallApp(): Boolean {return true}
        open fun isAppInstalled(): Boolean {return true}
    }

    fun startApp(context: Context) {
        callback.startApp(context)
    }

    fun installApp(): Boolean {
        handler.post {
            installStatus = AppInfo.InstallStatus.INSTALLING
            AppManager.listeners.forEach { it.onInstallStart(this) }
        }
        val result = callback.installApp()
        handler.post {
            if (result) {
                installStatus = AppInfo.InstallStatus.INSTALLED
                AppManager.listeners.forEach { it.onInstallSuccess(this) }
            } else {
                installStatus = AppInfo.InstallStatus.UNINSTALLED
                AppManager.listeners.forEach { it.onInstallFailure(this) }
            }
        }
        return result
    }

    fun uninstallApp(): Boolean {
        handler.post {
            installStatus = AppInfo.InstallStatus.UNINSTALLING
            AppManager.listeners.forEach { it.onUnInstallStart(this) }
        }
        val result = callback.uninstallApp()
        handler.post {
            if (result) {
                installStatus = AppInfo.InstallStatus.UNINSTALLED
                AppManager.listeners.forEach { it.onUnInstallSuccess(this) }
            } else {
                installStatus = AppInfo.InstallStatus.INSTALLED
                AppManager.listeners.forEach { it.onUnInstallFailure(this) }
            }
        }
        return result
    }

    fun isAppInstalled(): Boolean {
        return callback.isAppInstalled()
    }

}
