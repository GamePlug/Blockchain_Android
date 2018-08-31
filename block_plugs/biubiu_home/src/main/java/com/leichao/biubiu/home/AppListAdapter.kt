package com.leichao.biubiu.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leichao.util.ToastUtil
import kotlinx.android.synthetic.main.adapter_app_list.view.*

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
            holder.itemView.setOnClickListener { ToastUtil.show(app.appName) }
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return beanList.size
    }

    inner class BlogListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
