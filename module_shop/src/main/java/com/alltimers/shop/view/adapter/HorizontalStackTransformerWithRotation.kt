package com.alltimers.shop.view.adapter

import android.view.View
import androidx.annotation.NonNull
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max


class HorizontalStackTransformerWithRotation : ViewPager.PageTransformer {
    override fun transformPage(@NonNull view: View, position: Float) {
        view.elevation = 5f * (1 - abs(position)) + 1f
        val scale: Float = if (position < 0) (1 - SCALE_MAX) * position + 1 else (SCALE_MAX - 1) * position + 1
        val alpha: Float = if (position < 0) (1 - ALPHA_MAX) * position + 1 else (ALPHA_MAX - 1) * position + 1
        if (position < 0) {
            view.pivotX = view.width.toFloat()
            view.pivotY = view.height / 2f
        } else {
            view.pivotX = 0f
            view.pivotY = view.height / 2f
        }
        view.scaleX = scale
        view.scaleY = scale
        view.alpha = max(alpha, 0.4f)
        view.translationX = -view.width * position * 0.5f
    }

    companion object {
        const val SCALE_MAX = 0.8f
        const val ALPHA_MAX = 0.5f
    }


}