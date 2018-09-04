package com.leichao.common.proxy

object DroidPluginProxy: BaseProxy("com.leichao.biubiu.bridge.DroidPluginBridge") {

    fun install(filepath: String): Boolean {
        val result = invoke("install", filepath)
        return result as? Boolean ?: false
    }

    fun uninstall(packageName: String): Boolean {
        val result = invoke("uninstall", packageName)
        return result as? Boolean ?: false
    }

    fun isInstalled(packageName: String): Boolean {
        val result = invoke("isInstalled", packageName)
        return result as? Boolean ?: false
    }

}
