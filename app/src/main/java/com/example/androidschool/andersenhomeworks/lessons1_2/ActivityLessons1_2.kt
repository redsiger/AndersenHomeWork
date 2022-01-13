package com.example.androidschool.andersenhomeworks.lessons1_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.androidschool.andersenhomeworks.R

val MAIN_ACTIVITY_TAG = ActivityLessons1_2::class.java.name
val MAIN_ACTIVITY_COUNTER = "MAIN_ACTIVITY_COUNTER"

class ActivityLessons1_2 : AppCompatActivity() {

    private var mCount = 0
    private val tvCount by lazy { findViewById<TextView>(R.id.activityLesson1_2_tvCount) }
    private val btnCount by lazy { findViewById<Button>(R.id.activityLesson1_2_btnCount) }
    private val btnToast by lazy { findViewById<Button>(R.id.activityLesson1_2_btnToast) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson1_2)

        Log.v(MAIN_ACTIVITY_TAG, "Hello World! from verbose log")
        Log.d(MAIN_ACTIVITY_TAG, "Hello World! from debug log")
        Log.i(MAIN_ACTIVITY_TAG, "Hello world! from info log")
        Log.w(MAIN_ACTIVITY_TAG, "Hello world! from warn log")
        Log.e(MAIN_ACTIVITY_TAG, "Hello world! from error log")

        savedInstanceState?.let {
            mCount = it.getInt(MAIN_ACTIVITY_COUNTER)
            setCount(tvCount)
        }
        fundamentals_1_2()
    }

    /**
     * Codelab Fundamentals 1.2
     */
    private fun fundamentals_1_2() {

        btnToast.setOnClickListener {
            showToast()
        }

        btnCount.setOnClickListener {
            countUp()
            setCount(tvCount)
        }

    }

    private fun showToast() {
        Toast.makeText(this, R.string.activityLesson1_2_toastMessage, Toast.LENGTH_SHORT).show()
    }

    private fun countUp() {
        mCount++
    }

    private fun setCount(view: TextView) {
        view.text = mCount.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MAIN_ACTIVITY_COUNTER, mCount)
    }
}