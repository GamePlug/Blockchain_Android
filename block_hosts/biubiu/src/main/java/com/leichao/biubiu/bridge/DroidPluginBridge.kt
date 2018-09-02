package com.leichao.biubiu.bridge

import com.morgoo.droidplugin.pm.PluginManager

object DroidPluginBridge {
    fun installPackage(filepath: String, flags: Int): Int {
        return PluginManager.getInstance().installPackage(filepath, 0)
    }
}