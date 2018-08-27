package com.leichao.biubiu.home

import com.leichao.common.BaseActivity
import com.leichao.util.StatusBarUtil

class HomeActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.activity_home)
        StatusBarUtil.setStatusBarTranslucent(this)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

}