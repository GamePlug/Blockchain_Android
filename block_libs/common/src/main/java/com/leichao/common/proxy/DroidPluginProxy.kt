package com.leichao.common.proxy

import android.graphics.drawable.Drawable

object DroidPluginProxy: BaseProxy("com.leichao.biubiu.bridge.DroidPluginBridge") {

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
    fun getAllInstalled(): List<DroidApp> {
        val result = invoke("getAllInstalled")
        return result as? ArrayList<DroidApp> ?: ArrayList()
    }

    data class DroidApp(
            var filePath: String,
            var packageName: String,
            var appName: String,
            var appIcon: Drawable
    )

}