package com.leichao.biubiu.home.app.copy

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.leichao.biubiu.home.AppInfo
import com.leichao.biubiu.home.AppManager
import com.leichao.biubiu.home.R
import com.leichao.common.BaseActivity
import kotlinx.android.synthetic.main.activity_app_copy.*

class AppCopyActivity : BaseActivity(), AppManager.OnInstallListener {

    private var mAdapter: AppCopyAdapter? = null

    override fun initView() {
        setContentView(R.layout.activity_app_copy)
        title_bar.setTitleText("应用分身")
    }

    override fun initData() {
        copy_rv.layoutManager = LinearLayoutManager(this)
        copy_rv.itemAnimator = DefaultItemAnimator()
        Thread(Runnable {// 开启线程，因为中AppCopyAdapter获取了App列表，为耗时操作
            mAdapter = AppCopyAdapter(this)
            runOnUiThread { copy_rv.adapter = mAdapter }
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