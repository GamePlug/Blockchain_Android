package com.leichao.biubiu.home

import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.leichao.biubiu.home.app.copy.AppCopyActivity
import com.leichao.common.proxy.Plugin
import com.leichao.common.proxy.ProxyDroidPlugin
import com.leichao.common.proxy.ProxyRePlugin
import com.leichao.util.AppUtil

object AppManager {

    private const val MIN_NUM = 24
    private val handler = Handler(Looper.getMainLooper())
    private val appList = ArrayList<AppInfo>()
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
                    InstallChanged.UPDATE_SUCCESS -> addApp(app)
                    InstallChanged.INSTALL_FAILURE,
                    InstallChanged.UNINSTALL_SUCCESS -> removeApp(app)
                    else -> {}
                }
            }
        })
    }

    // 空白填充
    private fun fillEmptyApp() {
        val emptyNum = if (appList.size <= MIN_NUM) MIN_NUM - appList.size else appList.size % 4
        for (i in 1..emptyNum) appList.add(emptyApp)
    }

    private fun addApp(app: AppInfo) {
        if (!appList.contains(app)) {
            // 寻找第一个空位置添加
            run {
                appList.forEachIndexed { index, appInfo ->
                    if (appInfo.type == AppInfo.Type.EMPTY) {
                        appList[index] = app
                        return@run
                    }
                }
            }
            // 若未找到空位添加到末尾
            if (!appList.contains(app)) appList.add(app)
        }
        fillEmptyApp()
    }

    private fun removeApp(app: AppInfo) {
        if (appList.contains(app)) {
            appList[appList.indexOf(app)] = emptyApp
        }
        fillEmptyApp()
    }

    fun getAppList(): ArrayList<AppInfo> {
        if (appList.isEmpty()) {
            // 系统应用
            val defaultIcon = ContextCompat.getDrawable(AppUtil.getApp(), R.mipmap.ic_launcher)!!
            val defaultPackageName = AppUtil.getApp().packageName
            appList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "设置", defaultIcon, AppCopyActivity::class.java.name, defaultPackageName, ""
            )))
            appList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "回收站", defaultIcon, AppCopyActivity::class.java.name, defaultPackageName, ""
            )))
            appList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "关于我们", defaultIcon, AppCopyActivity::class.java.name, defaultPackageName, ""
            )))
            appList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "应用商店", defaultIcon, AppCopyActivity::class.java.name, defaultPackageName, ""
            )))
            appList.add(AppInfo(AppInfo.Type.SYSTEM, AppInfo.Status.INSTALLED, 0, Plugin(
                    "应用分身", defaultIcon, AppCopyActivity::class.java.name, defaultPackageName, ""
            )))
            // RePlugin应用
            for (pluginApp in ProxyRePlugin.getAllInstalled()) {
                appList.add(AppInfo(AppInfo.Type.PLUGIN_RE, AppInfo.Status.INSTALLED, 0, pluginApp))
            }
            // DroidPlugin应用
            for (pluginApp in ProxyDroidPlugin.getAllInstalled()) {
                appList.add(AppInfo(AppInfo.Type.PLUGIN_DROID, AppInfo.Status.INSTALLED, 0, pluginApp))
            }
            // 空白填充
            fillEmptyApp()
        }
        return appList
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
