package com.leichao.biubiu.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.leichao.common.proxy.Plugin
import com.leichao.common.proxy.ProxyDroidPlugin
import com.leichao.common.proxy.ProxyRePlugin
import com.qihoo360.replugin.RePlugin

data class AppInfo(
        var type: Type,
        var status: Status,
        var sort: Int,
        var plugin: Plugin
) {
    constructor(type: Type) :
            this(type, Status.UNINSTALLED, 0, Plugin())

    enum class Type {
        //系统---RePlugin---DroidPlugin----空
        SYSTEM, PLUGIN_RE, PLUGIN_DROID, EMPTY
    }

    enum class Status {
        //已安装------安装中-----升级中------未安装--------卸载中
        INSTALLED, INSTALLING, UPDATING,  UNINSTALLED, UNINSTALLING
    }

    fun startApp(context: Context) {
        when (type) {
            Type.SYSTEM -> {
                val intent = Intent()
                intent.component = ComponentName(plugin.packageName, plugin.pluginName)
                context.startActivity(intent)
            }
            Type.PLUGIN_RE -> {
                val activities = RePlugin.fetchPackageInfo(plugin.pluginName).activities
                if (activities != null && activities.isNotEmpty()) {
                    val intent = RePlugin.createIntent(plugin.pluginName, activities[0].name)
                    intent?.let { context.startActivity(it) }
                }
            }
            Type.PLUGIN_DROID -> {
                val intent = context.packageManager.getLaunchIntentForPackage(plugin.packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent?.let { context.startActivity(it) }
            }
            else -> {}
        }
    }

    fun installApp(): Boolean {
        val isUpdate = isAppInstalled()
        if (isUpdate) {
            status = AppInfo.Status.UPDATING
            AppManager.installChanged(this, AppManager.InstallChanged.UPDATE_START)
        } else {
            status = AppInfo.Status.INSTALLING
            AppManager.installChanged(this, AppManager.InstallChanged.INSTALL_START)
        }
        val result = when (type) {
            Type.SYSTEM -> true
            Type.PLUGIN_RE -> ProxyRePlugin.install(plugin.filePath)
            Type.PLUGIN_DROID -> ProxyDroidPlugin.install(plugin.filePath)
            else -> false
        }
        if (result) {
            status = AppInfo.Status.INSTALLED
            if (isUpdate) {
                AppManager.installChanged(this, AppManager.InstallChanged.UPDATE_SUCCESS)
            } else {
                AppManager.installChanged(this, AppManager.InstallChanged.INSTALL_SUCCESS)
            }
        } else {
            status = AppInfo.Status.UNINSTALLED
            if (isUpdate) {
                AppManager.installChanged(this, AppManager.InstallChanged.UPDATE_FAILURE)
            } else {
                AppManager.installChanged(this, AppManager.InstallChanged.INSTALL_FAILURE)
            }
        }
        return result
    }

    fun uninstallApp(): Boolean {
        status = AppInfo.Status.UNINSTALLING
        AppManager.installChanged(this, AppManager.InstallChanged.UNINSTALL_START)
        val result = when (type) {
            Type.SYSTEM -> true
            Type.PLUGIN_RE -> ProxyRePlugin.uninstall(plugin.pluginName)
            Type.PLUGIN_DROID -> ProxyDroidPlugin.uninstall(plugin.packageName)
            else -> false
        }
        if (result) {
            status = AppInfo.Status.UNINSTALLED
            AppManager.installChanged(this, AppManager.InstallChanged.UNINSTALL_SUCCESS)
        } else {
            status = AppInfo.Status.INSTALLED
            AppManager.installChanged(this, AppManager.InstallChanged.UNINSTALL_FAILURE)
        }
        return result
    }

    fun isAppInstalled(): Boolean {
        return when (type) {
            Type.SYSTEM -> true
            Type.PLUGIN_RE -> ProxyRePlugin.isInstalled(plugin.pluginName)
            Type.PLUGIN_DROID -> ProxyDroidPlugin.isInstalled(plugin.packageName)
            else -> false
        }
    }

}
