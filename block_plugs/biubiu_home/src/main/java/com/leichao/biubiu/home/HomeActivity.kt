package com.leichao.biubiu.home

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.common.BaseActivity
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), AppManager.OnInstallListener {

    private var mAdapter : AppListAdapter? = null

    override fun initView() {
        setContentView(R.layout.activity_home)
        StatusBarUtil.setFullTranslucent(this)
    }

    override fun initData() {
        home_rv.layoutManager = GridLayoutManager(this, 4)
        home_rv.itemAnimator = DefaultItemAnimator()
        Thread(Runnable {// 开启线程，因为中AppListAdapter获取了App列表，为耗时操作
            mAdapter = AppListAdapter(this)
            runOnUiThread {
                home_rv.adapter = mAdapter
                mAdapter?.let { ItemTouchHelper(AppDragCallback(it)).attachToRecyclerView(home_rv) }
            }
        }).start()
    }

    override fun initEvent() {
        AppManager.addInstallListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeInstallListener(this)
    }

    override fun onInstallChanged(app: AppInfo, installChanged: AppManager.InstallChanged) {
        mAdapter?.notifyDataSetChanged()
    }

}