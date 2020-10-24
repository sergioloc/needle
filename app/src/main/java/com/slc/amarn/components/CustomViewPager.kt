package com.slc.amarn.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var active = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (active) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (active) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(active: Boolean) {
        this.active = active
    }

}