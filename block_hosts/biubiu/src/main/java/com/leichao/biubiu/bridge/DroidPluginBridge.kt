package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.morgoo.droidplugin.pm.PluginManager

@Keep
object DroidPluginBridge {

    @Keep
    fun installPackage(filepath: String, flags: Int): Int {
        return PluginManager.getInstance().installPackage(filepath, flags)
    }

}
