package com.leichao.common.proxy

object RePluginProxy: BaseProxy("com.leichao.biubiu.bridge.RePluginBridge") {

    fun install(filepath: String): Boolean {
        val result = invoke("install", filepath)
        return result as? Boolean ?: false
    }

    fun uninstall(pluginName: String): Boolean {
        val result = invoke("uninstall", pluginName)
        return result as? Boolean ?: false
    }

    fun isInstalled(pluginName: String): Boolean {
        val result = invoke("isInstalled", pluginName)
        return result as? Boolean ?: false
    }

}
