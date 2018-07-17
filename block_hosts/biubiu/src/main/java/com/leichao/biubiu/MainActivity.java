package com.leichao.biubiu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("biubiu_home", "com.leichao.biubiu.home.MainActivity"));
    }
}
