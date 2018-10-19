package com.leichao.biubiu.home.app

import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.leichao.biubiu.home.MainActivity
import com.leichao.biubiu.home.R
import com.leichao.biubiu.home.app.copy.CopyActivity
import com.leichao.common.plugin.Plugin
import com.leichao.common.plugin.PluginConstant
import com.leichao.common.plugin.ProxyDroidPlugin
import com.leichao.common.plugin.ProxyRePlugin
import com.leichao.util.AppUtil
import com.qihoo360.replugin.RePlugin

object AppManager {

    private const val MIN_NUM = 24
    private val handler = Handler(Looper.getMainLooper())
    private val homeAppList = ArrayList<AppInfo>()
    private val copyAppList = ArrayList<AppInfo>()
    private val emptyApp = AppInfo(AppInfo.Type.EMPTY)
    private val listeners = ArrayList<OnInstallListener>()

    init {
        // 添加监听器到第一个
        listeners.add(0, object : OnInstallListener {
            override fun onInstallChanged(app: AppInfo, installChanged: InstallChanged) {
                when (installChanged) {
                    InstallChanged.INSTALL_START,
                    InstallChanged.INSTALL_SUCCESS,
                    InstallChanged.UPDATE_START,
                    InstallChanged.UPDATE_SUCCESS -> addHomeApp(app)
                    InstallChanged.INSTALL_FAILURE,
                    InstallChanged.UNINSTALL_SUCCESS -> removeHomeApp(app)
                    else -> {}
                }
            }
        })
    }

    fun getHomeAppList(): ArrayList<AppInfo> {
        if (homeAppList.isEmpty()) {
            // 系统应用
            val defaultIcon = ContextCompat.getDrawable(AppUtil.getApp(), R.mipmap.ic_launcher)!!
            val defaultPackageName = AppUtil.getApp().packageName
            homeAppList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "设置", defaultIcon, MainActivity::class.java.name, defaultPackageName, ""
            )))
            homeAppList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "回收站", defaultIcon, CopyActivity::class.java.name, defaultPackageName, ""
            )))
            homeAppList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "关于我们", defaultIcon, CopyActivity::class.java.name, defaultPackageName, ""
            )))
            homeAppList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "应用商店", defaultIcon, CopyActivity::class.java.name, defaultPackageName, ""
            )))
            homeAppList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "应用分身", defaultIcon, CopyActivity::class.java.name, defaultPackageName, ""
            )))
            // RePlugin应用
            for (pluginApp in ProxyRePlugin.getAllInstalled()) {
                if (pluginApp.pluginName != PluginConstant.BIUBIU_HOME) {// 排除首页插件
                    homeAppList.add(AppInfo(AppInfo.Type.PLUGIN_RE, AppInfo.Status.INSTALLED, 0, pluginApp))
                }
            }
            // DroidPlugin应用
            for (pluginApp in ProxyDroidPlugin.getAllInstalled()) {
                homeAppList.add(AppInfo(AppInfo.Type.PLUGIN_DROID, AppInfo.Status.INSTALLED, 0, pluginApp))
            }
            // 空白填充
            fillHomeApp()
        }
        return homeAppList
    }

    fun getCopyAppList(): java.util.ArrayList<AppInfo> {
        if (copyAppList.isEmpty()) {
            val packageManager = AppUtil.getApp().packageManager
            val apps = packageManager.getInstalledApplications(0)
            val installedApps = getHomeAppList()
            for (info in apps) {
                if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0 //非系统应用
                        && info.packageName != AppUtil.getApp().packageName // 排除该应用自身
                        && info.packageName != RePlugin.fetchPackageInfo(PluginConstant.BIUBIU_HOME)?.packageName) {// 排除首页插件
                    val filePath = info.sourceDir
                    val packageName = info.packageName
                    val appName = info.loadLabel(packageManager).toString()
                    val appIcon = info.loadIcon(packageManager)
                    var app: AppInfo? = null
                    run {
                        installedApps.forEach {
                            if (it.type == AppInfo.Type.PLUGIN_DROID && packageName == it.plugin.packageName) {
                                app = it
                                return@run
                            }
                        }
                    }
                    if (app != null) {
                        app?.let {
                            if (!copyAppList.contains(it)) {
                                it.plugin.filePath = filePath// 更新安装包地址
                                copyAppList.add(it)
                            }
                        }
                    } else {
                        copyAppList.add(AppInfo(AppInfo.Type.PLUGIN_DROID, AppInfo.Status.UNINSTALLED, 0, Plugin(
                                appName, appIcon, packageName, packageName, filePath
                        )))
                    }
                }
            }
        }
        return copyAppList
    }

    fun getEmptyApp(): AppInfo {
        return emptyApp
    }

    fun installChanged(app: AppInfo, installChanged: InstallChanged) {
        handler.post {
            listeners.forEach { it.onInstallChanged(app, installChanged) }
        }
    }

    fun addInstallListener(listener: OnInstallListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun removeInstallListener(listener: OnInstallListener) {
        if (listeners.contains(listener)) listeners.remove(listener)
    }

    private fun addHomeApp(app: AppInfo) {
        if (!homeAppList.contains(app)) {
            // 寻找第一个空位置添加
            run {
                homeAppList.forEachIndexed { index, appInfo ->
                    if (appInfo.type == AppInfo.Type.EMPTY) {
                        homeAppList[index] = app
                        return@run
                    }
                }
            }
            // 若未找到空位添加到末尾
            if (!homeAppList.contains(app)) homeAppList.add(app)
        }
        fillHomeApp()
    }

    private fun removeHomeApp(app: AppInfo) {
        if (homeAppList.contains(app)) {
            homeAppList[homeAppList.indexOf(app)] = emptyApp
        }
        fillHomeApp()
    }

    // 空白填充
    private fun fillHomeApp() {
        val emptyNum = if (homeAppList.size <= MIN_NUM) MIN_NUM - homeAppList.size else homeAppList.size % 4
        for (i in 1..emptyNum) homeAppList.add(emptyApp)
    }

    interface OnInstallListener {
        fun onInstallChanged(app: AppInfo, installChanged: InstallChanged)
    }

    enum class InstallChanged {
        //开始安装---------安装成功----------安装失败
        INSTALL_START, INSTALL_SUCCESS, INSTALL_FAILURE,
        //开始升级---------升级成功----------升级失败
        UPDATE_START, UPDATE_SUCCESS, UPDATE_FAILURE,
        //开始卸载-----------卸载成功------------卸载失败
        UNINSTALL_START, UNINSTALL_SUCCESS, UNINSTALL_FAILURE
    }

}
