package com.leichao.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

var a = 115

class User {
    val a = 116
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val hello = findViewById<TextView>(R.id.hello)
        hello.text = "Kotlin:" + a + ":" + User().a
    }
}