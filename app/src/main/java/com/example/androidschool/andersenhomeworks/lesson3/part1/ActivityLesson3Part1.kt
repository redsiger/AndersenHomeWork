package com.example.androidschool.andersenhomeworks.lesson3.part1

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson3Part1Binding

class ActivityLesson3Part1: AppCompatActivity() {

    private lateinit var _binding: ActivityLesson3Part1Binding
    private val mBinding get() = _binding
    private val requestOptions: RequestOptions by lazy {
        RequestOptions()
            .centerCrop()
    }
    // Lister for intercept Glide errors
    private val requestListener: RequestListener<Drawable> by lazy {
        object: RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                showToast(e!!.message)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLesson3Part1Binding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        initLoadButton()
    }

    private fun initLoadButton() {
        mBinding.activityLesson3part1ImgLoadBtn.setOnClickListener {
            if (mBinding.activityLesson3part1ImgUrlInput.text.isNotEmpty()) {
                val imgUrl = mBinding.activityLesson3part1ImgUrlInput.text.toString()
                loadImg(imgUrl)
            } else {
                showToast(getString(R.string.activityLesson3_part1_imgInputEmpty))
            }
        }
    }

    private fun loadImg(imgUrl: String) {
        try {
            Glide
                .with(this)
                .load(imgUrl)
                .apply(requestOptions)
                .addListener(requestListener)
                .into(mBinding.activityLesson3part1Img)
        } catch (e: Exception) {
            showToast(e.message)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}