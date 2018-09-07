package com.leichao.common.plugin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

object ProxyDroidPlugin: ProxyBase("com.leichao.biubiu.bridge.DroidPluginBridge") {

    /**
     * 安装DroidPlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    fun install(filePath: String): Boolean {
        val result = invoke("install", filePath)
        return result as? Boolean ?: false
    }

    /**
     * 卸载DroidPlugin插件。
     */
    fun uninstall(packageName: String): Boolean {
        val result = invoke("uninstall", packageName)
        return result as? Boolean ?: false
    }

    /**
     * 判断是否安装DroidPlugin插件。
     */
    fun isInstalled(packageName: String): Boolean {
        val result = invoke("isInstalled", packageName)
        return result as? Boolean ?: false
    }

    /**
     * 获取所有已安装的DroidPlugin插件。
     */
    @Suppress("UNCHECKED_CAST")
    fun getAllInstalled(): List<Plugin> {
        val result = invoke("getAllInstalled")
        val list = result as? ArrayList<Map<String, Any>> ?: ArrayList()
        val droidAppList = ArrayList<Plugin>()
        for (info in list) {
            droidAppList.add(Plugin(
                    info["appName"] as? String ?: "",
                    info["appIcon"] as? Drawable ?: ColorDrawable(),
                    info["packageName"] as? String ?: "",
                    info["packageName"] as? String ?: "",
                    info["filePath"] as? String ?: ""
            ))
        }
        return droidAppList
    }

}
