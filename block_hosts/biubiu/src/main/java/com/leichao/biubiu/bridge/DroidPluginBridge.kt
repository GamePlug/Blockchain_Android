package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.leichao.util.AppUtil
import com.morgoo.droidplugin.pm.PluginManager
import com.morgoo.helper.compat.PackageManagerCompat

@Keep
object DroidPluginBridge {

    @Keep
    fun install(filepath: String): Boolean {
        val thatInfo = AppUtil.getApp().packageManager.getPackageArchiveInfo(filepath, 0)
        if (thatInfo != null) {
            val lastInfo = PluginManager.getInstance().getPackageInfo(thatInfo.packageName, 0)
            if (lastInfo == null) {
                return PluginManager.getInstance().installPackage(filepath, 0) == PackageManagerCompat.INSTALL_SUCCEEDED
            } else if (thatInfo.versionCode >= lastInfo.versionCode) {
                return PluginManager.getInstance().installPackage(filepath, PackageManagerCompat.INSTALL_REPLACE_EXISTING) == PackageManagerCompat.INSTALL_SUCCEEDED
            }
        }
        return false
    }

    @Keep
    fun uninstall(packageName: String): Boolean {
        return PluginManager.getInstance().deletePackage(packageName, 0) == PackageManagerCompat.DELETE_SUCCEEDED
    }

    @Keep
    fun isInstalled(packageName: String): Boolean {
        return PluginManager.getInstance().isPluginPackage(packageName)
    }

}
