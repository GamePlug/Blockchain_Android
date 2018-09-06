package com.leichao.biubiu.home

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.common.BaseActivity
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private lateinit var mBeanList : ArrayList<AppInfo>
    private lateinit var mAdapter : AppListAdapter
    private lateinit var listener: AppManager.OnInstallListener

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
    }

    override fun initEvent() {
        ItemTouchHelper(AppDragCallback(mBeanList, mAdapter)).attachToRecyclerView(home_rv)
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