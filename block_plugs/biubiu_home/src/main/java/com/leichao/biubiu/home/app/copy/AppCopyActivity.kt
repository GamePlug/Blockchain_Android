package com.leichao.biubiu.home.app.copy

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.leichao.biubiu.home.AppInfo
import com.leichao.biubiu.home.AppManager
import com.leichao.biubiu.home.R
import com.leichao.common.BaseActivity
import kotlinx.android.synthetic.main.activity_app_copy.*

class AppCopyActivity: BaseActivity() {

    private lateinit var mBeanList : ArrayList<AppInfo>
    private lateinit var mAdapter : AppCopyAdapter
    private lateinit var listener: AppManager.OnInstallListener

    override fun initView() {
        setContentView(R.layout.activity_app_copy)
        title_bar.setTitleText("应用分身")
    }

    override fun initData() {
        copy_rv.layoutManager = LinearLayoutManager(this)
        copy_rv.itemAnimator = DefaultItemAnimator()
        mBeanList = ArrayList()
        mAdapter = AppCopyAdapter(this, mBeanList)
        copy_rv.adapter = mAdapter

        mBeanList.addAll(AppCopyManager.appList)
        mAdapter.notifyDataSetChanged()
    }

    override fun initEvent() {
        listener = object : AppManager.SimpleInstallListener() {
            override fun onStatusChanged(app: AppInfo) {
                mAdapter.notifyDataSetChanged()
            }
        }
        AppManager.addInstallListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeInstallListener(listener)
    }

}