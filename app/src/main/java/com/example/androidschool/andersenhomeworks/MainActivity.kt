package com.example.androidschool.andersenhomeworks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidschool.andersenhomeworks.databinding.ActivityMainBinding
import com.example.androidschool.andersenhomeworks.lesson3.part1.ActivityLesson3Part1
import com.example.androidschool.andersenhomeworks.lesson3.part2.ActivityLesson3Part2
import com.example.androidschool.andersenhomeworks.lesson4.ActivityLesson4
import com.example.androidschool.andersenhomeworks.lesson5.ActivityLesson5
import com.example.androidschool.andersenhomeworks.lesson6.ActivityLesson6
import com.example.androidschool.andersenhomeworks.lessons1_2.ActivityLessons1_2

class MainActivity: AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

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
        initLesson3Part2Button()
        initLesson4Button()
        initLesson5Button()
        initLesson6Button()
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

    private fun initLesson3Part2Button() {
        mBinding.activityLesson3Part2LaunchBtn .setOnClickListener {
            val intent = Intent(this, ActivityLesson3Part2::class.java)
            startActivity(intent)
        }
    }

    private fun initLesson4Button() {
        mBinding.activityLesson4LaunchBtn.setOnClickListener {
            val intent = Intent(this, ActivityLesson4::class.java)
            startActivity(intent)
        }
    }

    private fun initLesson5Button() {
        mBinding.activityLesson5LaunchBtn.setOnClickListener {
            val intent = Intent(this, ActivityLesson5::class.java)
            startActivity(intent)
        }
    }

    private fun initLesson6Button() {
        mBinding.activityLesson6LaunchBtn.setOnClickListener {
            val intent = Intent(this, ActivityLesson6::class.java)
            startActivity(intent)
        }
    }
}