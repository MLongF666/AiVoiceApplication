package com.example.lib_base.trasformer

import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/7/10 20:46
 * @version: 1.0
 */
class ScaleInTransformer : ViewPager2.PageTransformer {
    private val TAG = "ScaleInTransformer"
    override fun transformPage(view: View, position: Float) {
        view.elevation = -abs(position)
        val pageWidth = view.width
        val pageHeight = view.height

        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()
        if (position < -1) {
            Log.d(TAG, "$position")
            view.scaleX = DEFAULT_MIN_SCALE
            view.scaleY = DEFAULT_MIN_SCALE
            view.pivotX = pageWidth.toFloat()
        } else if (position <= 1) {
            if (position < 0) {
                Log.d(TAG, "$position")
                val scaleFactor = (1 + position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
            } else {
                Log.d(TAG, "$position")
                val scaleFactor = (1 - position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * ((1 - position) * DEFAULT_CENTER)
            }
        } else {
            Log.d(TAG, "$position")
            view.pivotX = 0f
            view.scaleX = DEFAULT_MIN_SCALE
            view.scaleY = DEFAULT_MIN_SCALE
        }
    }

    companion object {
        const val DEFAULT_MIN_SCALE: Float = 0.85f
        const val DEFAULT_CENTER: Float = 0.5f
    }
}