package com.leichao.biubiu.home.app.copy

import com.leichao.biubiu.home.R
import com.leichao.common.BaseActivity
import kotlinx.android.synthetic.main.activity_app_copy.*

class AppCopyActivity: BaseActivity() {

    override fun initView() {
        setContentView(R.layout.activity_app_copy)
        title_bar.setTitleText("应用分身")
    }

    override fun initData() {
        AppCopy().getAppList(this)
    }

    override fun initEvent() {
    }

}