package com.sandyz.alltimers.schedule.view.castom

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import com.sandyz.alltimers.common.extensions.dp2px
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.schedule.R
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SnapDelete @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private var expandedInst: WeakReference<SnapDelete>? = null
    }

    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val mScroller by lazy {
        Scroller(context)
    }

    private val mFlingRunnable = object : Runnable {
        override fun run() {
            Log.e("sandyzhang", "run")
            if ((mScroller.computeScrollOffset() || mOffset >= mMaxTranslationX) && mScroller.currVelocity > 500f) {
                mOffset = mScroller.currX.toFloat()
                mHandler.post(this)
                isMoving = true
            } else {
                touchFinish()
            }
        }
    }


    private val done: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_done, this, false)

    private val content: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_content, this, false)
    private val optionView1: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_top, this, false)
    private val optionView2: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_edit, this, false)
    private val optionView3: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_del, this, false)
    private val shadowView: View =
        LayoutInflater.from(context).inflate(R.layout.schedule_layout_shadow, this, false).apply {
            alpha = 0f
        }

    private var mWidthSize = 0
    private var mMaxTranslationX = 0
    private var mAnimator: Animator? = null

    var action1: (() -> Unit)? = null
        set(value) {
            field = value
            optionView1.setOnClickAction {
                field?.invoke()
            }
        }
    var action2: (() -> Unit)? = null
        set(value) {
            field = value
            optionView2.setOnClickAction {
                field?.invoke()
            }
        }
    var action3: (() -> Unit)? = null
        set(value) {
            field = value
            optionView3.setOnClickAction {
                field?.invoke()
            }
        }
    private var isExpanded = false

    private var mOffset = 0f
        set(value) {
            /**
             * 禁止左方滑动
             */
            field = max(0f, value)
            field = min(field, mMaxTranslationX.toFloat())
            applyTranslation()
        }
    private var originTouchX = 0f
//    private var originTouchY = 0f

    init {
        addView(optionView3)
        addView(optionView2)
        addView(optionView1)
        addView(done)
        addView(content)
        addView(shadowView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidthSize = w
        content.layoutParams.width = MeasureSpec.makeMeasureSpec(mWidthSize, MeasureSpec.EXACTLY)
        done.layoutParams.width = MeasureSpec.makeMeasureSpec(mWidthSize, MeasureSpec.EXACTLY)
        optionView1.layoutParams.width =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(optionView1.measuredWidth), MeasureSpec.EXACTLY)
        optionView2.layoutParams.width =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(optionView2.measuredWidth), MeasureSpec.EXACTLY)
        optionView3.layoutParams.width =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(optionView3.measuredWidth), MeasureSpec.EXACTLY)
        shadowView.layoutParams.width =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(shadowView.measuredWidth), MeasureSpec.EXACTLY)
        mMaxTranslationX =
            MeasureSpec.getSize(optionView3.measuredWidth) + MeasureSpec.getSize(optionView2.measuredWidth) + MeasureSpec.getSize(optionView1.measuredWidth) - 3 * context.dp2px(
                12
            )
        applyTranslation()
    }

    private fun applyTranslation() {
        content.layoutParams.apply {
            if (this is LayoutParams) {
                leftMargin = -mOffset.toInt()
                requestLayout()
            }
        }
        done.layoutParams.apply {
            if (this is LayoutParams) {
                leftMargin = -mOffset.toInt() - mWidthSize
                done.alpha = 1 - (-mOffset / mWidthSize)
                requestLayout()
            }
        }
        val optionView1Width = MeasureSpec.getSize(optionView1.measuredWidth) - context.dp2px(12)
        val optionView2Width = MeasureSpec.getSize(optionView2.measuredWidth) - context.dp2px(12)
        val optionView3Width = MeasureSpec.getSize(optionView3.measuredWidth) - context.dp2px(12)
        val left1 = -(mOffset / mMaxTranslationX) * (mMaxTranslationX - optionView1Width) + mWidthSize - optionView1Width - context.dp2px(12)
        val left2 =
            -(mOffset / mMaxTranslationX) * (mMaxTranslationX - optionView1Width - optionView2Width) + mWidthSize - optionView2Width - context.dp2px(
                12
            )
        val left3 =
            -(mOffset / mMaxTranslationX) * (mMaxTranslationX - optionView1Width - optionView2Width - optionView3Width) + mWidthSize - optionView3Width - context.dp2px(
                12
            )
        shadowView.alpha = (mOffset / mMaxTranslationX) * 0.9f
        optionView1.layoutParams.apply {
            if (this is LayoutParams) {
                leftMargin = left1.toInt()
                requestLayout()
            }
        }
        optionView2.layoutParams.apply {
            if (this is LayoutParams) {
                leftMargin = left2.toInt()
                requestLayout()
            }
        }
        optionView3.layoutParams.apply {
            if (this is LayoutParams) {
                leftMargin = left3.toInt()
                requestLayout()
            }
        }
    }

    private var isMoving = false

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mScroller.forceFinished(false)
                mHandler.removeCallbacks(mFlingRunnable)

                mAnimator?.cancel()
                isMoving = false
                originTouchX = if (isExpanded) {
                    event.x + mOffset
                } else {
                    event.x + mOffset
                }

//                originTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(originTouchX - event.x) > 20f) {
                    isMoving = true
                    if (expandedInst?.get() != this) {
                        expandedInst?.get()?.resumeAnim()
                        expandedInst = WeakReference(this)
                    }
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                if (isMoving) {
                    mOffset = originTouchX - event.x
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                touchFinish()
            }
        }
        return isMoving
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }


    private val velocityTracker by lazy { VelocityTracker.obtain() }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("sandyzhang", "onTouchEvent" + isMoving)
        event ?: return false

        velocityTracker.addMovement(event)
        velocityTracker.computeCurrentVelocity(1000);
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (isMoving) {
                    mOffset = originTouchX - event.x
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                touchFinish()
                startFling(-velocityTracker.xVelocity)

            }
        }
        performClick()

        return isMoving
    }

    private fun touchFinish() {
        isMoving = false
        if (mOffset > 0) {
            // 左滑
            if (mOffset > mMaxTranslationX / 2f) {
                // 展开
                expandAnim()
            } else {
                // 恢复
                resumeAnim()
            }
        } else {
            // 右滑删除
            if (-mOffset > mWidthSize * 3f / 4f) {
                // 删除
                deleteAnim()
            } else {
                // 恢复
                resumeAnim()
            }
        }
    }

    fun resumeAnim(duration: Long = 350L) {
        mHandler.removeCallbacks(mFlingRunnable)
        isExpanded = false
        if (mAnimator?.isStarted == true) {
            mAnimator?.cancel()
        }
        mAnimator = ValueAnimator.ofFloat(mOffset, 0f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            this.duration = duration
            addUpdateListener { mOffset = it.animatedValue as Float }
        }
        mAnimator?.start()
    }

    private fun expandAnim() {
        mHandler.removeCallbacks(mFlingRunnable)
        isExpanded = true

        if (mAnimator?.isStarted == true) {
            mAnimator?.cancel()
        }
        mAnimator = ValueAnimator.ofFloat(mOffset, mMaxTranslationX.toFloat()).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 350L
            addUpdateListener { mOffset = it.animatedValue as Float }
        }
        mAnimator?.start()
    }

    private fun deleteAnim() {
        isExpanded = true

        if (mAnimator?.isStarted == true) {
            mAnimator?.cancel()
        }
        mAnimator = ValueAnimator.ofFloat(mOffset, -mWidthSize.toFloat()).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 350L
            addUpdateListener {
                mOffset = it.animatedValue as Float
            }

        }
        mAnimator?.start()
    }

    private fun startFling(vx: Float) {
        mAnimator?.cancel()
        mHandler.removeCallbacks(mFlingRunnable)
        mScroller.fling(mOffset.toInt(), 0, vx.toInt(), 0, -mWidthSize, mMaxTranslationX, 0, 0)
        mHandler.post(mFlingRunnable)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}