package com.leichao.biubiu.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leichao.common.bridge.DroidPluginBridge
import com.leichao.util.PermissionUtil
import com.leichao.util.ToastUtil
import kotlinx.android.synthetic.main.adapter_app_list.view.*
import java.io.File

class AppListAdapter(private val context: Context, private val beanList: List<AppInfo>) : RecyclerView.Adapter<AppListAdapter.BlogListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false)
        return BlogListHolder(view)
    }

    override fun onBindViewHolder(holder: BlogListHolder, position: Int) {
        val app = beanList[position]
        if (app.appType != AppInfo.AppType.EMPTY) {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.app_name.text = app.appName
            holder.itemView.app_icon.setImageResource(app.appIcon)
            holder.itemView.setOnClickListener {
                ToastUtil.show(app.appName)
                if (app.appType == AppInfo.AppType.PLUGIN_DROID && app.appName == "摩拉旅行") {
                    if (DroidPluginBridge.isInstalled("com.mula.travel")) {
                        startTravelPlugin()
                    } else {
                        installTravelPlugin()
                    }
                }
            }
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return beanList.size
    }

    inner class BlogListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun installTravelPlugin() {
        PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE) { grantedList, _ ->
            if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Thread(Runnable {
                    // 安装插件
                    val result = DroidPluginBridge.install(Environment.getExternalStorageDirectory().toString()
                            + File.separator + "Plugins" + File.separator + "mula_travel.apk")
                    // 启动插件
                    if (result == 1) startTravelPlugin()
                }).start()
            }
        }
    }

    private fun startTravelPlugin() {
        val intent = context.packageManager.getLaunchIntentForPackage("com.mula.travel")
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}
