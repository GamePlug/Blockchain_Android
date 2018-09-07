package com.leichao.biubiu.home.app.copy

import android.Manifest
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leichao.biubiu.home.app.AppInfo
import com.leichao.biubiu.home.app.AppManager
import com.leichao.biubiu.home.R
import com.leichao.util.PermissionUtil
import com.leichao.util.ToastUtil
import kotlinx.android.synthetic.main.adapter_app_copy.view.*

class CopyAdapter(private val context: Context) : RecyclerView.Adapter<CopyAdapter.BlogListHolder>() {

    private val beanList = AppManager.getCopyAppList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_app_copy, parent, false)
        return BlogListHolder(view)
    }

    override fun onBindViewHolder(holder: BlogListHolder, position: Int) {
        val app = beanList[position]
        holder.itemView.visibility = View.VISIBLE
        holder.itemView.app_icon.setImageDrawable(app.plugin.appIcon)
        if (app.status == AppInfo.Status.UNINSTALLED) {
            holder.itemView.app_name.text = app.plugin.appName
            holder.itemView.app_name.setTextColor(ContextCompat.getColor(context, R.color.color_333333))
            holder.itemView.setOnClickListener {
                PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE) { grantedList, _ ->
                    if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Thread(Runnable {
                            ToastUtil.show("开始安装${app.plugin.appName}")
                            if (app.installApp()) {
                                ToastUtil.show("${app.plugin.appName}安装完成")
                            } else {
                                ToastUtil.show("${app.plugin.appName}安装失败")
                            }
                        }).start()
                    }
                }
            }
        } else {
            when {
                app.status == AppInfo.Status.INSTALLED -> holder.itemView.app_name.text = "${app.plugin.appName}(已安装)"
                app.status == AppInfo.Status.INSTALLING -> holder.itemView.app_name.text = "${app.plugin.appName}(安装中)"
                app.status == AppInfo.Status.UPDATING -> holder.itemView.app_name.text = "${app.plugin.appName}(更新中)"
                app.status == AppInfo.Status.UNINSTALLING -> holder.itemView.app_name.text = "${app.plugin.appName}(卸载中)"
            }
            holder.itemView.app_name.setTextColor(ContextCompat.getColor(context, R.color.color_999999))
            holder.itemView.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int {
        return beanList.size
    }

    inner class BlogListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
