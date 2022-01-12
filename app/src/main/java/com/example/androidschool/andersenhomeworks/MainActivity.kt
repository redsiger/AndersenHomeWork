package com.example.androidschool.andersenhomeworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

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

        fundamentals_1_2()
    }

    /**
     * Codelab Fundamentals 1.2
     */

    private var mCount = 0

    private fun fundamentals_1_2() {

        val btnToast = findViewById<Button>(R.id.activityMain_btnToast)
        btnToast.setOnClickListener {
            showToast()
        }

        val tvCount = findViewById<TextView>(R.id.activityMain_tvCount)

        val btnCount = findViewById<Button>(R.id.activityMain_btnCount)
        btnCount.setOnClickListener {
            countUp(tvCount)
        }

    }

    private fun showToast() {
        Toast.makeText(this, R.string.activityMain_toastMessage, Toast.LENGTH_SHORT).show()
    }

    private fun countUp(view: TextView) {
        mCount++
        view.text = mCount.toString()
    }
}