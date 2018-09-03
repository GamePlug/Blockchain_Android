package com.leichao.common.bridge

object DroidPluginBridge: BaseBridge("com.leichao.biubiu.bridge.DroidPluginBridge") {

    fun install(filepath: String): Int {
        val result = invoke("install", filepath)
        return result as? Int ?: -1
    }

    fun update(filepath: String): Int {
        val result = invoke("update", filepath)
        return result as? Int ?: -1
    }

    fun uninstall(packageName: String) {
        invoke("uninstall", packageName)
    }

    fun isInstalled(packageName: String): Boolean {
        val result = invoke("isInstalled", packageName)
        return result as? Boolean ?: false
    }

}