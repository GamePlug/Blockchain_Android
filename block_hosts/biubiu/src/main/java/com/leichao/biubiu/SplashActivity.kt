package com.leichao.biubiu

import com.leichao.common.base.BaseActivity
import com.leichao.common.plugin.PluginConstant
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
            val pluginName = PluginConstant.BIUBIU_HOME
            if (RePlugin.preload(pluginName)) {
                val delayMillis = if (System.currentTimeMillis() - startTime > 1000L) 0L else 1000L
                splash_text.postDelayed({
                    val activities = RePlugin.fetchPackageInfo(pluginName).activities
                    if (activities != null && activities.isNotEmpty()) {
                        RePlugin.startActivity(this@SplashActivity,
                                RePlugin.createIntent(pluginName, activities[0].name))
                    }
                    finish()
                }, delayMillis)
            }
        }).start()
    }

    override fun initEvent() {
        splash_text.setOnClickListener { ToastUtil.show("启动页") }
    }

}
