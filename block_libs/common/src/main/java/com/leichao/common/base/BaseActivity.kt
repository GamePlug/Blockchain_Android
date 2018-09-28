package com.leichao.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initEvent()
    }

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initEvent()
}
