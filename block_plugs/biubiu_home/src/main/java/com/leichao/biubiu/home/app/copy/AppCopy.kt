package com.leichao.biubiu.home.app.copy

import android.content.Context
import android.content.pm.ApplicationInfo
import com.leichao.util.LogUtil

class AppCopy {

    fun getAppList(context: Context) {
        val apps = context.packageManager.getInstalledApplications(0)
        for (info in apps) {
            if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                //非系统应用
                LogUtil.e("非系统应用: ${info.sourceDir}")
                val packageName = info.packageName
                val sourceDir = info.sourceDir
                val icon = info.loadIcon(context.packageManager)
            } else {
                LogUtil.e("系统应用: ${info.sourceDir}")
            }
        }
    }

}