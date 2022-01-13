package com.example.androidschool.andersenhomeworks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidschool.andersenhomeworks.databinding.ActivityMainBinding
import com.example.androidschool.andersenhomeworks.lesson3.part1.ActivityLesson3Part1
import com.example.androidschool.andersenhomeworks.lessons1_2.ActivityLessons1_2

class MainActivity: AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val mBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        initButtons()
    }

    private fun initButtons() {
        initLessons1_2Button()
        initLesson3Part1Button()
    }

    private fun initLessons1_2Button() {
        mBinding.activityLessons12LaunchBtn.setOnClickListener {
            val intent = Intent(this, ActivityLessons1_2::class.java)
            startActivity(intent)
        }
    }

    private fun initLesson3Part1Button() {
        mBinding.activityLesson3Part1LaunchBtn.setOnClickListener {
            val intent = Intent(this, ActivityLesson3Part1::class.java)
            startActivity(intent)
        }
    }
}