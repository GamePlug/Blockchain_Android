package com.leichao.biubiu

import com.leichao.common.BaseActivity
import com.leichao.util.StatusBarUtil
import com.leichao.util.ToastUtil
import com.qihoo360.replugin.RePlugin
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.activity_splash)
        StatusBarUtil.setFullTranslucent(this)
        StatusBarUtil.setTextColor(this, true)
    }

    override fun initData() {
        val startTime = System.currentTimeMillis()
        Thread(Runnable {
            val pluginName = "biubiu_home"
            if (RePlugin.preload(pluginName)) {
                val delayMillis = if (System.currentTimeMillis() - startTime > 1000L) 0L else 2000L
                splash_text.postDelayed({
                    RePlugin.startActivity(this@SplashActivity,
                            RePlugin.createIntent(pluginName, "com.leichao.biubiu.home.HomeActivity"))
                    finish()
                }, delayMillis)
            }
        }).start()
    }

    override fun initEvent() {
        splash_text.setOnClickListener { ToastUtil.show("启动页") }
    }

}
