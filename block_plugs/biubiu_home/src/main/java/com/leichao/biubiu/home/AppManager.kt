package com.leichao.biubiu.home

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.leichao.biubiu.home.app.copy.AppCopyActivity
import com.leichao.common.proxy.DroidPluginProxy
import com.leichao.common.proxy.RePluginProxy
import com.qihoo360.replugin.RePlugin
import java.io.File
import java.util.*

object AppManager {

    val appList = ArrayList<AppInfo>()
    val emptyApp = AppInfo(AppInfo.AppType.EMPTY, "", 0)

    init {
        // 系统应用
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "设置", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "回收站", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "关于我们", R.mipmap.ic_launcher))
        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "应用商店", R.mipmap.ic_launcher))

        appList.add(AppInfo(AppInfo.AppType.SYSTEM, "应用分身", R.mipmap.ic_launcher, 0, object : AppInfo.Callback() {
            override fun startApp(context: Context) {
                context.startActivity(Intent(context, AppCopyActivity::class.java))
            }
        }))

        appList.add(AppInfo(AppInfo.AppType.PLUGIN_RE, "桌面", R.mipmap.ic_launcher, 0, object : AppInfo.Callback() {
            override fun startApp(context: Context) {
                val intent = RePlugin.createIntent("biubiu_home", "com.leichao.biubiu.home.HomeActivity")
                intent?.let { context.startActivity(it) }
            }

            override fun installApp(): Boolean {
                return RePluginProxy.install("")
            }

            override fun uninstallApp(): Boolean {
                return RePluginProxy.uninstall("biubiu_home")
            }

            override fun isAppInstalled(): Boolean {
                return RePluginProxy.isInstalled("biubiu_home")
            }
        }))

        appList.add(AppInfo(AppInfo.AppType.PLUGIN_DROID, "摩拉旅行", R.mipmap.ic_launcher, 0, object : AppInfo.Callback() {
            override fun startApp(context: Context) {
                val intent = context.packageManager.getLaunchIntentForPackage("com.mula.travel")
                intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent?.let { context.startActivity(it) }
            }

            override fun installApp(): Boolean {
                return DroidPluginProxy.install("${Environment.getExternalStorageDirectory()}${File.separator}Plugins${File.separator}mula_travel.apk")
            }

            override fun uninstallApp(): Boolean {
                return DroidPluginProxy.uninstall("com.mula.travel")
            }

            override fun isAppInstalled(): Boolean {
                return DroidPluginProxy.isInstalled("com.mula.travel")
            }
        }))

        for (i in 1..15) {
            appList.add(AppInfo(AppInfo.AppType.PLUGIN_RE, "应用$i", R.mipmap.ic_launcher))
        }
        // 空白填充
        val emptyNum = if (appList.size <= 24) 24 - appList.size else appList.size % 4
        for (i in 1..emptyNum) {
            appList.add(emptyApp)
        }
    }

}
