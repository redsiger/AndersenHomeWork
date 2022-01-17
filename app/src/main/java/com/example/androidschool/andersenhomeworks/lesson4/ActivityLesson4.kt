package com.example.androidschool.andersenhomeworks.lesson4

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidschool.andersenhomeworks.R

class ActivityLesson4: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson4)

        val clockView = findViewById<ClockView>(R.id.clockView)
        val startBtn = findViewById<Button>(R.id.start)
        val stopBtn = findViewById<Button>(R.id.stop)

        startBtn.setOnClickListener {
            clockView.start()
        }
        stopBtn.setOnClickListener {
            clockView.stop()
        }
    }
}