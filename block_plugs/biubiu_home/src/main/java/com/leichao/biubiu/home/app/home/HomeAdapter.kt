package com.leichao.biubiu.home.app.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leichao.biubiu.home.app.AppInfo
import com.leichao.biubiu.home.app.AppManager
import com.leichao.biubiu.home.R
import com.leichao.util.ToastUtil
import kotlinx.android.synthetic.main.adapter_app_list.view.*

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.BlogListHolder>() {

    private val beanList = AppManager.getHomeAppList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false)
        return BlogListHolder(view)
    }

    override fun onBindViewHolder(holder: BlogListHolder, position: Int) {
        val app = beanList[position]
        if (app.type != AppInfo.Type.EMPTY) {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.app_name.text = app.plugin.appName
            holder.itemView.app_icon.setImageDrawable(app.plugin.appIcon)
            holder.itemView.setOnClickListener {
                when (app.status) {
                    AppInfo.Status.INSTALLED -> {
                        app.startApp(context)
                    }
                    AppInfo.Status.INSTALLING -> {
                        ToastUtil.show("安装中")
                    }
                    AppInfo.Status.UPDATING -> {
                        ToastUtil.show("更新中")
                    }
                    AppInfo.Status.UNINSTALLING -> {
                        ToastUtil.show("卸载中")
                    }
                    AppInfo.Status.UNINSTALLED -> {
                        ToastUtil.show("未安装")
                    }
                }
            }
        } else {
            holder.itemView.visibility = View.GONE
            holder.itemView.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int {
        return beanList.size
    }

    inner class BlogListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
