package com.sandyz.alltimers.common.widgets.backgroundscroll

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.common_dialog_choose.view.*
import kotlin.math.abs

/**
 * 作为ScrollBackgroundView的子view使用
 */
class ScrollFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 是否在长按拖动修改位置
    var isScrolling = false
        set(value) {
            field = value
            if (value) {
                startAnim()
                bringToFront()
            } else {
                cancelAnim()
            }
        }

    // 背景拖动时，是否要随着背景拖动
    var shouldScrollWithBackground = true

    private val scrollCallback = Runnable { isScrolling = true }

    // 用于判断是单击还是拖动
    private var originX = 0f
    private var originY = 0f

    // 长按抖动的动画效果
    private val animator: ObjectAnimator =
        ObjectAnimator.ofFloat(this, "rotation", 5f, 0f, -5f, 0f).setDuration(250L)

    // 用于拖动时计算delta距离
    private var originLeft = 0
    private var originTop = 0
    private var lastX = 0f
    private var lastY = 0f

    // 计算拖动的总偏移量
    private var totalDX = 0f
    private var totalDY = 0f
    var scrollChild: ScrollChild? = null

    init {
        setWillNotDraw(false)
        clipChildren = false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                removeCallbacks(scrollCallback)
                originX = event.rawX
                originY = event.rawY
                lastX = event.rawX
                lastY = event.rawY
                totalDX = 0f
                totalDY = 0f
                val layoutParams = layoutParams as FrameLayout.LayoutParams
                originLeft = layoutParams.leftMargin
                originTop = layoutParams.topMargin
                postDelayed(scrollCallback, 800L)
                parent.requestDisallowInterceptTouchEvent(true)

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(event.rawX - originX) > 10f || abs(event.rawY - originY) > 10f) {
                    removeCallbacks(scrollCallback)
                    if (!isScrolling) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                if (isScrolling) {
                    removeCallbacks(scrollCallback)
                    fixPosition(event)
                }
                return isScrolling
            }
            MotionEvent.ACTION_UP -> {
                if (!isScrolling) {
                    removeCallbacks(scrollCallback)
                    performClick()
                }
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


    private fun fixPosition(event: MotionEvent) {
        if (parent !is ScrollBackgroundView) {
            return
        }
        val layoutParams = layoutParams as FrameLayout.LayoutParams
        val dx = event.rawX - lastX
        val dy = event.rawY - lastY
        totalDX += dx
        totalDY += dy
        layoutParams.leftMargin = (originLeft + totalDX + 0.5f).toInt()
        layoutParams.topMargin = (originTop + totalDY + 0.5f).toInt()
        lastX = event.rawX
        lastY = event.rawY
        parent.requestLayout()
    }

    private fun startAnim() {
        cancelAnim()
        animator.repeatCount = -1
        animator.start()
    }

    private fun cancelAnim() {
        if (animator.isStarted) {
            animator.end()

        }
    }

    fun getName(): String? {
        return scrollChild?.getName()
    }

    fun onBind() {
        scrollChild?.onBind(this)
    }
}