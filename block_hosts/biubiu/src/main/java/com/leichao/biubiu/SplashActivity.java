package com.leichao.biubiu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.leichao.util.ToastUtil;
import com.qihoo360.replugin.RePlugin;

public class SplashActivity extends AppCompatActivity {

    TextView tvSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvSplash = findViewById(R.id.splash_text);

        tvSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("启动页");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pluginName = "biubiu_home";
                if (RePlugin.preload(pluginName)) {
                    tvSplash.post(new Runnable() {
                        @Override
                        public void run() {
                            RePlugin.startActivity(SplashActivity.this,
                                    RePlugin.createIntent(pluginName, "com.leichao.biubiu.home.HomeActivity"));
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

}
