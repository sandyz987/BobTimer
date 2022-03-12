package com.sandyz.alltimers.common.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import com.sandyz.alltimers.common.R

/**
 *@author zhangzhe
 *@date 2022/2/20
 *@description
 */

fun View.setOnClickAction(action: (() -> Unit)?) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.alpha = 0.6f
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.x >= 0 && event.x <= v.width && event.y >= 0 && event.y <= height) {
                    v.alpha = 0.6f
                } else {
                    v.alpha = 1f
                }
            }
            MotionEvent.ACTION_UP -> {
                if (event.x >= 0 && event.x <= v.width && event.y >= 0 && event.y <= height) {
                    action?.invoke()
                }
                v.alpha = 1f
            }
            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1f
            }
        }
        parent?.requestDisallowInterceptTouchEvent(true)
        true
    }
}

fun View.setOnClickActionWithScale(action: (() -> Unit)?) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.setTag(R.id.common_tag_btn_size, Pair(width, height))
                downAnim(v)
            }
            MotionEvent.ACTION_MOVE -> {
                val sizePair = v.getTag(R.id.common_tag_btn_size) as? Pair<Float, Float>
                if (sizePair != null) {
                    if (event.x >= 0 && event.x <= sizePair.first && event.y >= 0 && event.y <= sizePair.second) {
                        downAnim(v)
                    } else {
                        upAnim(v)
                    }
                } else {
                    if (event.x >= 0 && event.x <= v.width && event.y >= 0 && event.y <= height) {
                        downAnim(v)
                    } else {
                        upAnim(v)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val sizePair = v.getTag(R.id.common_tag_btn_size) as? Pair<Float, Float>
                if (sizePair != null) {
                    if (event.x >= 0 && event.x <= sizePair.first && event.y >= 0 && event.y <= sizePair.second) {
                        action?.invoke()
                    }
                } else {
                    if (event.x >= 0 && event.x <= v.width && event.y >= 0 && event.y <= height) {
                        action?.invoke()
                    }
                }
                upAnim(v)
            }
            MotionEvent.ACTION_CANCEL -> {
                upAnim(v)
            }
        }
        parent?.requestDisallowInterceptTouchEvent(true)
        true
    }
}

private fun downAnim(v: View) {
    val lastAnim = v.getTag(R.id.common_tag_btn_anim)
    if (lastAnim != null) {
        (lastAnim as? AnimatorSet)?.cancel()
    }

    val anim1 = ObjectAnimator.ofFloat(v, "scaleX", v.scaleX, 0.8f)
    val anim2 = ObjectAnimator.ofFloat(v, "scaleY", v.scaleY, 0.8f)
    val animSet = AnimatorSet().apply {
        duration = 70L
    }
    animSet.play(anim1).with(anim2)
    v.setTag(R.id.common_tag_btn_anim, animSet)
    animSet.start()
}

private fun upAnim(v: View) {
    val lastAnim = v.getTag(R.id.common_tag_btn_anim)
    if (lastAnim != null) {
        (lastAnim as? AnimatorSet)?.cancel()
    }

    val anim1 = ObjectAnimator.ofFloat(v, "scaleX", v.scaleX, 1f)
    val anim2 = ObjectAnimator.ofFloat(v, "scaleY", v.scaleY, 1f)
    val animSet = AnimatorSet().apply {
        duration = 70L
    }
    animSet.play(anim1).with(anim2)
    v.setTag(R.id.common_tag_btn_anim, animSet)
    animSet.start()
}