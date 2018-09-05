package com.leichao.biubiu.home

import android.Manifest
import android.os.Environment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.common.BaseActivity
import com.leichao.common.proxy.DroidPluginProxy
import com.leichao.util.PermissionUtil
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File

class HomeActivity : BaseActivity() {

    private lateinit var mBeanList : ArrayList<AppInfo>
    private lateinit var mAdapter : AppListAdapter

    override fun initView() {
        setContentView(R.layout.activity_home)
        StatusBarUtil.setFullTranslucent(this)
    }

    override fun initData() {
        home_rv.layoutManager = GridLayoutManager(this, 4)
        home_rv.itemAnimator = DefaultItemAnimator()
        mBeanList = ArrayList()
        mAdapter = AppListAdapter(this, mBeanList)
        home_rv.adapter = mAdapter

        mBeanList.addAll(AppManager.appList)
        mAdapter.notifyDataSetChanged()
        checkInstallApp()
    }

    override fun initEvent() {
        ItemTouchHelper(AppDragCallback(mBeanList, mAdapter)).attachToRecyclerView(home_rv)
    }

    private fun checkInstallApp() {
        PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE) { grantedList, _ ->
            if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mBeanList.forEach {
                    if (!it.callback.isAppInstalled()) {
                        Thread(Runnable {
                            it.callback.installApp()
                        }).start()
                    }
                }
                DroidPluginProxy.install("${Environment.getExternalStorageDirectory()}${File.separator}Plugins${File.separator}mula_travel.apk")
            }
        }
    }

}