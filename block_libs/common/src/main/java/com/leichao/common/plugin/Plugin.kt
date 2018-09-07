package com.leichao.common.plugin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

data class Plugin(
        var appName: String,
        var appIcon: Drawable,
        var pluginName: String,
        var packageName: String,
        var filePath: String
) {
    constructor(): this("", ColorDrawable(), "", "", "")
}
