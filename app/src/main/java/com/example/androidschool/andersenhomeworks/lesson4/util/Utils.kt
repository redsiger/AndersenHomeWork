package com.example.androidschool.andersenhomeworks.lesson4.util

import android.content.Context

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}