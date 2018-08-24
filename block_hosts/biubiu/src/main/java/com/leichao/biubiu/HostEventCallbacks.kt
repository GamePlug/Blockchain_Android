package com.leichao.biubiu

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.qihoo360.replugin.RePluginEventCallbacks
import com.qihoo360.replugin.model.PluginInfo

class HostEventCallbacks(context: Context) : RePluginEventCallbacks(context) {

    override fun onInstallPluginFailed(path: String?, code: RePluginEventCallbacks.InstallResult?) {
        super.onInstallPluginFailed(path, code)
    }

    override fun onInstallPluginSucceed(info: PluginInfo?) {
        super.onInstallPluginSucceed(info)
    }

    override fun onStartActivityCompleted(plugin: String?, activity: String?, result: Boolean) {
        super.onStartActivityCompleted(plugin, activity, result)
    }

    override fun onPrepareAllocPitActivity(intent: Intent?) {
        super.onPrepareAllocPitActivity(intent)
    }

    override fun onPrepareStartPitActivity(context: Context?, intent: Intent?, pittedIntent: Intent?) {
        super.onPrepareStartPitActivity(context, intent, pittedIntent)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        super.onActivityDestroyed(activity)
    }

    override fun onBinderReleased() {
        super.onBinderReleased()
    }
}
