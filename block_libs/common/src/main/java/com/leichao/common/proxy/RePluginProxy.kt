package com.leichao.common.proxy

object RePluginProxy: BaseProxy("com.leichao.biubiu.bridge.RePluginBridge") {

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

}
