package com.leichao.biubiu.home.app.home

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.biubiu.home.app.AppInfo
import com.leichao.biubiu.home.app.AppManager
import com.leichao.biubiu.home.R
import com.leichao.common.base.BaseActivity
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), AppManager.OnInstallListener {

    private var mAdapter : HomeAdapter? = null

    override fun initView() {
        setContentView(R.layout.activity_home)
        StatusBarUtil.setFullTranslucent(this)
    }

    override fun initData() {
        home_rv.layoutManager = GridLayoutManager(this, 4)
        home_rv.itemAnimator = DefaultItemAnimator()
        Thread(Runnable {// 开启线程，因为中AppListAdapter获取了App列表，为耗时操作
            mAdapter = HomeAdapter(this)
            runOnUiThread {
                home_rv.adapter = mAdapter
                mAdapter?.let { ItemTouchHelper(HomeDragCallback(it)).attachToRecyclerView(home_rv) }
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