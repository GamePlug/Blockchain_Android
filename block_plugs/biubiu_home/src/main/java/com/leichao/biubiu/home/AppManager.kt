package com.leichao.biubiu.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.leichao.biubiu.home.app.copy.AppCopyActivity
import com.leichao.common.bridge.DroidPluginBridge
import com.leichao.util.PermissionUtil
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
            override fun startActivity(context: Context) {
                context.startActivity(Intent(context, AppCopyActivity::class.java))
            }
        }))

        appList.add(AppInfo(AppInfo.AppType.PLUGIN_DROID, "摩拉旅行", R.mipmap.ic_launcher, 0, object : AppInfo.Callback() {
            override fun startActivity(context: Context) {
                val intent = context.packageManager.getLaunchIntentForPackage("com.mula.travel")
                if (DroidPluginBridge.isInstalled("com.mula.travel")) {
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                } else {
                    PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE) { grantedList, _ ->
                        if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Thread(Runnable {
                                // 安装插件
                                val result = DroidPluginBridge.install(Environment.getExternalStorageDirectory().toString()
                                        + File.separator + "Plugins" + File.separator + "mula_travel.apk")
                                // 启动插件
                                if (result == 1) {
                                    if (intent != null) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(intent)
                                    }
                                }
                            }).start()
                        }
                    }
                }
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
