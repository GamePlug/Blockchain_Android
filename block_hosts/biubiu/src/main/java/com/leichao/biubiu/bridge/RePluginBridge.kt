package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.qihoo360.replugin.RePlugin

@Keep
object RePluginBridge {

    /**
     * 安装RePlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    @Keep
    fun install(filePath: String): Boolean {
        return RePlugin.install(filePath) != null
    }

    /**
     * 卸载RePlugin插件。
     */
    @Keep
    fun uninstall(pluginName: String): Boolean {
        return RePlugin.uninstall(pluginName)
    }

    /**
     * 判断是否安装RePlugin插件。
     */
    @Keep
    fun isInstalled(pluginName: String): Boolean {
        return RePlugin.isPluginInstalled(pluginName)
    }

}
