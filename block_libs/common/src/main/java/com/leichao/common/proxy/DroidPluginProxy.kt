package com.leichao.common.proxy

object DroidPluginProxy: BaseProxy("com.leichao.biubiu.bridge.DroidPluginBridge") {

    /**
     * 安装DroidPlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    fun install(filepath: String): Boolean {
        val result = invoke("install", filepath)
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

}
