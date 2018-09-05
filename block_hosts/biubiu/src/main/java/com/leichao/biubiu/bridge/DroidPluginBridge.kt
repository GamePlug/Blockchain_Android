package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.leichao.util.AppUtil
import com.morgoo.droidplugin.pm.PluginManager
import com.morgoo.helper.compat.PackageManagerCompat

@Keep
object DroidPluginBridge {

    /**
     * 安装DroidPlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    @Keep
    fun install(filepath: String): Boolean {
        val insInfo = AppUtil.getApp().packageManager.getPackageArchiveInfo(filepath, 0)
        if (insInfo != null) {
            val curInfo = PluginManager.getInstance().getPackageInfo(insInfo.packageName, 0)
            return when {
                curInfo == null -> PluginManager.getInstance().installPackage(filepath, 0) == PackageManagerCompat.INSTALL_SUCCEEDED
                insInfo.versionCode >= curInfo.versionCode -> PluginManager.getInstance().installPackage(filepath, PackageManagerCompat.INSTALL_REPLACE_EXISTING) == PackageManagerCompat.INSTALL_SUCCEEDED
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

}
