package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.morgoo.droidplugin.pm.PluginManager
import com.morgoo.helper.compat.PackageManagerCompat

@Keep
object DroidPluginBridge {

    @Keep
    fun install(filepath: String): Int {
        return PluginManager.getInstance().installPackage(filepath, 0)
    }

    @Keep
    fun update(filepath: String): Int {
        return PluginManager.getInstance().installPackage(filepath, PackageManagerCompat.INSTALL_REPLACE_EXISTING)
    }

    @Keep
    fun uninstall(packageName: String) {
        PluginManager.getInstance().deletePackage(packageName, 0)
    }

    @Keep
    fun isInstalled(packageName: String): Boolean {
        return PluginManager.getInstance().isPluginPackage(packageName)
    }

}
