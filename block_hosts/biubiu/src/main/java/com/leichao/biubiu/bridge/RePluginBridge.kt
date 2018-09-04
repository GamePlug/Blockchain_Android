package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.qihoo360.replugin.RePlugin

@Keep
object RePluginBridge {

    @Keep
    fun install(filepath: String): Boolean {
        return RePlugin.install(filepath) != null
    }

    @Keep
    fun uninstall(pluginName: String): Boolean {
        return RePlugin.uninstall(pluginName)
    }

    @Keep
    fun isInstalled(pluginName: String): Boolean {
        return RePlugin.isPluginInstalled(pluginName)
    }

}
