package com.example.androidschool.andersenhomeworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

val MAIN_ACTIVITY_TAG = MainActivity::class.java.name

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(MAIN_ACTIVITY_TAG, "Hello World! from verbose log")
        Log.d(MAIN_ACTIVITY_TAG, "Hello World! from debug log")
        Log.i(MAIN_ACTIVITY_TAG, "Hello world! from info log")
        Log.w(MAIN_ACTIVITY_TAG, "Hello world! from warn log")
        Log.e(MAIN_ACTIVITY_TAG, "Hello world! from error log")
    }
}