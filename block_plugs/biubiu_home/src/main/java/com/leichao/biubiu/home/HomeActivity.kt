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
    private var appNum = 15

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

        val total = if (appNum <= 24) 24 else {
            4*(appNum/4 + if (appNum%4 == 0) 0 else 1)
        }
        for (i in 1..total) {
            if (i <= appNum)
                mBeanList.add(AppInfo(true, true, "设置$i", R.mipmap.ic_launcher))
            else
                mBeanList.add(AppInfo(false, false, "", 0))
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun initEvent() {
        ItemTouchHelper(AppDragCallback(mBeanList, mAdapter)).attachToRecyclerView(home_rv)
    }

}