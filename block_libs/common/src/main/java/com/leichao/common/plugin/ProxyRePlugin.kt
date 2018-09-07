package com.leichao.common.plugin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

object ProxyRePlugin: ProxyBase("com.leichao.biubiu.bridge.RePluginBridge") {

    /**
     * 安装RePlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    fun install(filePath: String): Boolean {
        val result = invoke("install", filePath)
        return result as? Boolean ?: false
    }

    /**
     * 卸载RePlugin插件。
     */
    fun uninstall(pluginName: String): Boolean {
        val result = invoke("uninstall", pluginName)
        return result as? Boolean ?: false
    }

    /**
     * 判断是否安装RePlugin插件。
     */
    fun isInstalled(pluginName: String): Boolean {
        val result = invoke("isInstalled", pluginName)
        return result as? Boolean ?: false
    }

    /**
     * 获取所有已安装的RePlugin插件。
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllInstalled(): List<Plugin> {
        val result = invoke("getAllInstalled")
        val list = result as? ArrayList<Map<String, Any>> ?: ArrayList()
        val reAppList = ArrayList<Plugin>()
        for (info in list) {
            reAppList.add(Plugin(
                    info["appName"] as? String ?: "",
                    info["appIcon"] as? Drawable ?: ColorDrawable(),
                    info["pluginName"] as? String ?: "",
                    info["packageName"] as? String ?: "",
                    info["filePath"] as? String ?: ""
            ))
        }
        return reAppList
    }

}
