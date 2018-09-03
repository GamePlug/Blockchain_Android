package com.leichao.common.bridge

object DroidPluginBridge: BaseBridge("com.leichao.biubiu.bridge.DroidPluginBridge") {

    fun installPackage(filepath: String, flags: Int): Int {
        return invoke("installPackage", filepath, flags) as Int
    }

}