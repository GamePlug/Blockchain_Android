package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.leichao.common.proxy.DroidPluginProxy
import com.leichao.util.AppUtil
import com.morgoo.droidplugin.pm.PluginManager
import com.morgoo.helper.compat.PackageManagerCompat

@Keep
object DroidPluginBridge {

    /**
     * 安装DroidPlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    @Keep
    fun install(filePath: String): Boolean {
        val insInfo = AppUtil.getApp().packageManager.getPackageArchiveInfo(filePath, 0)
        if (insInfo != null) {
            val curInfo = PluginManager.getInstance().getPackageInfo(insInfo.packageName, 0)
            return when {
                curInfo == null -> PluginManager.getInstance().installPackage(filePath, 0) == PackageManagerCompat.INSTALL_SUCCEEDED
                insInfo.versionCode >= curInfo.versionCode -> PluginManager.getInstance().installPackage(filePath, PackageManagerCompat.INSTALL_REPLACE_EXISTING) == PackageManagerCompat.INSTALL_SUCCEEDED
                else -> false
            }
        }
        return false
    }

    /**
     * 卸载DroidPlugin插件。
     */
    @Keep
    fun uninstall(packageName: String): Boolean {
        return PluginManager.getInstance().deletePackage(packageName, 0) == PackageManagerCompat.DELETE_SUCCEEDED
    }

    /**
     * 判断是否安装DroidPlugin插件。
     */
    @Keep
    fun isInstalled(packageName: String): Boolean {
        return PluginManager.getInstance().isPluginPackage(packageName)
    }

    /**
     * 获取所有已安装的DroidPlugin插件。
     */
    @Keep
    fun getAllInstalled(): List<DroidPluginProxy.DroidApp> {
        val packageManager = AppUtil.getApp().packageManager
        val apps = PluginManager.getInstance().getInstalledApplications(0)
        val droidAppList = ArrayList<DroidPluginProxy.DroidApp>()
        for (info in apps) {
            val filePath = info.sourceDir
            val packageName = info.packageName
            val appName = info.loadLabel(packageManager).toString()
            val appIcon = info.loadIcon(packageManager)
            droidAppList.add(DroidPluginProxy.DroidApp(filePath, packageName, appName, appIcon))
        }
        return droidAppList
    }

}
