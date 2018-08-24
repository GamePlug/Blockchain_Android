package com.leichao.biubiu

import android.widget.TextView
import com.leichao.common.BaseActivity
import com.leichao.util.ToastUtil
import com.qihoo360.replugin.RePlugin

class SplashActivity : BaseActivity() {

    lateinit var tvSplash: TextView

    override fun initView() {
        setContentView(R.layout.activity_splash)
        tvSplash = findViewById(R.id.splash_text)
    }

    override fun initData() {
        Thread(Runnable {
            val pluginName = "biubiu_home"
            if (RePlugin.preload(pluginName)) {
                tvSplash.post {
                    RePlugin.startActivity(this@SplashActivity,
                            RePlugin.createIntent(pluginName, "com.leichao.biubiu.home.HomeActivity"))
                    finish()
                }
            }
        }).start()
    }

    override fun initEvent() {
        tvSplash.setOnClickListener { ToastUtil.show("启动页") }
    }

}
