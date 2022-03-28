package com.sandyz.alltimers.myhome.backgroundscroll

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import com.sandyz.alltimers.common.extensions.fixToRange
import kotlin.math.abs

/**
 * 作为ScrollBackgroundView的子view使用
 */
class ScrollFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var mName: String = ""

    // 是否在长按拖动修改位置
    var isScrolling = false
        set(value) {
            field = value
            if (value) {
                startAnim()
                alpha = 0.6f
                bringToFront()
            } else {
                removeCallbacks(scrollCallback)
                cancelAnim()
                alpha = 1.0f
                gravityAnim()
            }
        }

    // 背景拖动时，是否要随着背景拖动
    var shouldScrollWithBackground = true

    // 是否可以长按拖动
    var canMove = true


    private val scrollCallback = Runnable { isScrolling = true }

    // 用于判断是单击还是拖动
    private var originX = 0f
    private var originY = 0f

    // 长按抖动的动画效果
    private val animator: ObjectAnimator =
        ObjectAnimator.ofFloat(this, "rotation", 0f, 3.5f, 0f, -3.5f, 0f).setDuration(250L)

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
                if (!canMove) return false
                removeCallbacks(scrollCallback)
                originX = event.rawX
                originY = event.rawY
                lastX = event.rawX
                lastY = event.rawY
                totalDX = 0f
                totalDY = 0f
                val layoutParams = layoutParams as LayoutParams
                originLeft = layoutParams.leftMargin
                originTop = layoutParams.topMargin
                postDelayed(scrollCallback, ((parent as? ScrollBackgroundView?)?.longDragTime) ?: 800L)
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
                if (!canMove) return false
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
                } else {
                    if ((parent as? ScrollBackgroundView?)?.endDrayImmediately == true) {
                        isScrolling = false
                    }
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
        val layoutParams = layoutParams as LayoutParams
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

    fun getWidgetType(): String? {
        return scrollChild?.getWidgetType()
    }

    fun getWidgetName(): String {
        return mName
    }

    fun onBind() {
        scrollChild?.onBind(this)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (scrollChild?.getWidgetType() == "fixed") return
        val parentScrollBgView = parent as? ScrollBackgroundView ?: return
        pivotX = (right - left) / 2f
        pivotY = (bottom - top) / 2f
        val focus = bottom - 0.1f * (bottom - top)
        if (focus > parentScrollBgView.getRealHorizon()) {
            val percent =
                ((focus - parentScrollBgView.getRealHorizon()) / (parentScrollBgView.getRealHeight() - parentScrollBgView.getRealHorizon()))
                    .fixToRange(0f, 1f)
            // 改变层次
            elevation = when (scrollChild?.viewElevation()) {
                1 -> 22f
                -1 -> 0.5f
                else -> 1f + 20f * percent
            }
            // 修改透视大小
            scaleX = 1f + percent * 0.4f
            scaleY = 1f + percent * 0.4f
        } else {
            elevation = 1f
            scaleX = 1f
            scaleY = 1f
        }
    }

    private var gravityAnimator: ValueAnimator? = null
    fun gravityAnim() {
        if (scrollChild?.withGravity() == false) return
        gravityAnimator?.cancel()
        val parentScrollBgView = parent as? ScrollBackgroundView ?: return
        val focus = bottom - 0.1f * (bottom - top)
        if (focus < parentScrollBgView.getRealHorizon()) {
            val start = top.toFloat()
            val target = parentScrollBgView.getRealHorizon() - 0.9f * (bottom - top)
            val percent = (target - start) / parentScrollBgView.getRealHorizon()
            gravityAnimator = ValueAnimator.ofFloat(start, target).apply {
                addUpdateListener {
                    removeCallbacks(scrollCallback)
                    if (parent !is ScrollBackgroundView) {
                        return@addUpdateListener
                    }
                    val layoutParams = layoutParams as LayoutParams
                    layoutParams.topMargin = (animatedValue as Float).toInt()
                    parent.requestLayout()
                }
                interpolator = BounceInterpolator()
                duration = (200L + 1000L * percent).toLong()
                start()
            }
        }
    }
}