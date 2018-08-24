package com.leichao.biubiu.home

import android.content.Intent
import com.leichao.common.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.activity_home)
    }

    override fun initData() {
        home_text.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun initEvent() {
    }

}