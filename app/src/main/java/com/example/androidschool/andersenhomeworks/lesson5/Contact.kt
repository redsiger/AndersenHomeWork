package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Int = -1,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val imgUrl: String = ""
): Parcelable
