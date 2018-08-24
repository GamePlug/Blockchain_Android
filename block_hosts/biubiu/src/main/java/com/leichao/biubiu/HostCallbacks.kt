package com.leichao.biubiu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import com.qihoo360.replugin.ContextInjector
import com.qihoo360.replugin.PluginDexClassLoader
import com.qihoo360.replugin.RePluginCallbacks
import com.qihoo360.replugin.RePluginClassLoader
import com.qihoo360.replugin.model.PluginInfo

import java.io.InputStream

class HostCallbacks(context: Context) : RePluginCallbacks(context) {

    override fun createClassLoader(parent: ClassLoader, original: ClassLoader): RePluginClassLoader {
        return super.createClassLoader(parent, original)
    }

    override fun createPluginClassLoader(pi: PluginInfo, dexPath: String, optimizedDirectory: String, librarySearchPath: String, parent: ClassLoader): PluginDexClassLoader {
        return super.createPluginClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent)
    }

    override fun onPluginNotExistsForActivity(context: Context?, plugin: String?, intent: Intent?, process: Int): Boolean {
        return super.onPluginNotExistsForActivity(context, plugin, intent, process)
    }

    override fun onLoadLargePluginForActivity(context: Context?, plugin: String?, intent: Intent?, process: Int): Boolean {
        return super.onLoadLargePluginForActivity(context, plugin, intent, process)
    }

    override fun getSharedPreferences(context: Context, name: String, mode: Int): SharedPreferences {
        return super.getSharedPreferences(context, name, mode)
    }

    override fun openLatestFile(context: Context, filename: String): InputStream {
        return super.openLatestFile(context, filename)
    }

    override fun createContextInjector(): ContextInjector {
        return super.createContextInjector()
    }

    override fun isPluginBlocked(pluginInfo: PluginInfo?): Boolean {
        return super.isPluginBlocked(pluginInfo)
    }

    override fun initPnPluginOverride() {
        super.initPnPluginOverride()
    }
}
