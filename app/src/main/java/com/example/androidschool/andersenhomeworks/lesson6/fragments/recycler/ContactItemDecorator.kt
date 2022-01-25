package com.example.androidschool.andersenhomeworks.lesson6.fragments.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersenhomeworks.util.dpToPx

class ContactItemDecorator(
    private val marginTop: Int = 0,
    private val marginBottom: Int = 0,
    private val marginLeft: Int = 0,
    private val marginRight: Int = 0,
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top = view.context.dpToPx(marginTop).toInt()
        outRect.bottom = view.context.dpToPx(marginBottom).toInt()
        outRect.left = view.context.dpToPx(marginLeft).toInt()
        outRect.right = view.context.dpToPx(marginRight).toInt()

    }
}