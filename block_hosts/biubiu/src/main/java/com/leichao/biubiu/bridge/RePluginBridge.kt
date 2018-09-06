package com.leichao.biubiu.bridge

import android.support.annotation.Keep
import com.leichao.util.AppUtil
import com.qihoo360.replugin.RePlugin

/**
 * RePlugin对外反射开放类，返回值使用Java JDK或Android SDK中的类，否者反射的返回值会出现转换异常
 */
@Keep
object RePluginBridge {

    /**
     * 安装RePlugin插件。支持安装，同版本覆盖安装，或升级。不支持降级。
     */
    @Keep
    fun install(filePath: String): Boolean {
        return RePlugin.install(filePath) != null
    }

    /**
     * 卸载RePlugin插件。
     */
    @Keep
    fun uninstall(pluginName: String): Boolean {
        return RePlugin.uninstall(pluginName)
    }

    /**
     * 判断是否安装RePlugin插件。
     */
    @Keep
    fun isInstalled(pluginName: String): Boolean {
        return RePlugin.isPluginInstalled(pluginName)
    }

    /**
     * 获取所有已安装的RePlugin插件。
     */
    @Keep
    fun getAllInstalled(): List<Map<String, Any>> {
        val list = ArrayList<Map<String, Any>>()
        for (rePlugin in RePlugin.getPluginInfoList()) {
            val packageManager = AppUtil.getApp().packageManager
            val packageInfo = RePlugin.fetchPackageInfo(rePlugin.name)
            val map = HashMap<String, Any>()
            map["filePath"] = rePlugin.apkFile.absolutePath
            map["packageName"] = rePlugin.packageName
            map["appName"] = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            map["appIcon"] = packageInfo.applicationInfo.loadIcon(packageManager)
            map["pluginName"] = rePlugin.name
            list.add(map)
        }
        return list
    }

}
